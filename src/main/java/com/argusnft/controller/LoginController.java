package com.argusnft.controller;

import com.argusnft.model.Login;
import com.argusnft.services.BlockfrostService;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.backend.api.AddressService;
import com.bloxbean.cardano.client.backend.model.AddressContent;
import com.bloxbean.cardano.client.cip.cip30.CIP30DataSigner;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.cip.cip8.COSESign1;
import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.client.util.Tuple;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final static Integer POLICY_ID_LENGTH = 56;

//    https://github.com/bloxbean/cardano-client-examples/blob/0.2.0-beta4/src/main/java/com/bloxbean/cardano/client/examples/SignVerifyDataTest.java

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final BlockfrostService blockfrostService;

    public LoginController(BlockfrostService blockfrostService) {
        this.blockfrostService = blockfrostService;
    }

    @PostMapping("/login")
    public void createEntity(@RequestBody Login login) throws JsonProcessingException {
        logger.info("HELLO");
        logger.info(login.getDataSignature());
        logger.info(login.getBaseAddress());
        logger.info(login.getMessage());

        //Load signature
        DataSignature dataSignature = DataSignature.from(login.getDataSignature());
        //verify
        boolean verified = CIP30DataSigner.INSTANCE.verify(dataSignature);
        logger.info(String.format("Verified? %s", verified));

    }

    @GetMapping("/utxos/{baseAddress}")
    public void getUtxos(@PathVariable String baseAddress) throws ApiException {
        AddressService addressService = blockfrostService.getAddressService();
        Result<AddressContent> addressInfo = addressService.getAddressInfo(baseAddress);
//        addressInfo.getValue().getAmount().forEach(System.out::println);
        addressInfo
                .getValue()
                .getAmount()
                .stream()
                .map(foo -> foo.getUnit())
                .filter(foo -> foo.startsWith("f0ff48bbb7bbe9d59a40f1ce90e9e9d0ff5002ec48f232b49ca0fb9a"))
                .map(foo -> {
                    String policyId = foo.substring(0, POLICY_ID_LENGTH);
                    String assetNameHex = foo.substring(POLICY_ID_LENGTH);
                    String assetName = new String(HexUtil.decodeHexString(assetNameHex));
                    return new Tuple(policyId, assetName);
                })
                .forEach(tuple -> System.out.printf("%s %s%n", tuple._1, tuple._2));
    }

}
