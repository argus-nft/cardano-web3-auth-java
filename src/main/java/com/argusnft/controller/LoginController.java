package com.argusnft.controller;

import com.argusnft.model.Login;
import com.argusnft.services.BlockfrostService;
import com.argusnft.services.JwtService;
import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.backend.api.AddressService;
import com.bloxbean.cardano.client.backend.model.AddressContent;
import com.bloxbean.cardano.client.backend.model.TxContentOutputAmount;
import com.bloxbean.cardano.client.cip.cip30.CIP30DataSigner;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.client.util.Tuple;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private static final String ADAHANDLE_POLICY_ID = "f0ff48bbb7bbe9d59a40f1ce90e9e9d0ff5002ec48f232b49ca0fb9a";

    private final static Integer POLICY_ID_LENGTH = 56;

    private final static String WEBSITE = "web3auth.coolwebsite.com";

    private final static Map<String, String> MESSAGES = new HashMap<>();

//    https://github.com/bloxbean/cardano-client-examples/blob/0.2.0-beta4/src/main/java/com/bloxbean/cardano/client/examples/SignVerifyDataTest.java

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final BlockfrostService blockfrostService;

    private final JwtService jwtService;

    public LoginController(BlockfrostService blockfrostService, JwtService jwtService) {
        this.blockfrostService = blockfrostService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) throws JsonProcessingException {
        //Load signature
        System.out.println(login.getDataSignature());
        DataSignature tmpSignature = DataSignature.from(login.getDataSignature());
        DataSignature dataSignature = new DataSignature(tmpSignature.signature(), tmpSignature.key());


        Address address = new Address(dataSignature.address());
        String s = address.toBech32();

        System.out.println(s);

        String nonce = MESSAGES.get(s);
        System.out.println("nonce: " + nonce);

        Optional<String> adaHandle = Arrays.stream(nonce
                        .split(";"))
                .filter(stuff -> stuff.trim().startsWith("ada_handle"))
                .findFirst()
                .map(handle -> handle.split(":")[1]);

        System.out.println(nonce.equals(new String(dataSignature.coseSign1().payload())));

        //verify
        boolean verified = CIP30DataSigner.INSTANCE.verify(dataSignature);
        logger.info(String.format("Verified? %s", verified));

        Optional<String> token = jwtService.createToken(s, adaHandle);

        return token.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @GetMapping("/nonce")
    public String getNonce(@RequestParam("base_address") String baseAddress, @RequestParam(value = "ada_handle", required = false) Optional<String> adahandle) throws ApiException {
        logger.info("base_address: " + baseAddress);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.format("%s;%s", WEBSITE, timestamp);
        if (adahandle.isPresent()) {
            nonce = String.format("%s;ada_handle:%s", nonce, adahandle.get());
        }
        MESSAGES.put(baseAddress, nonce);
        return nonce;
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
                .map(TxContentOutputAmount::getUnit)
                .filter(foo -> foo.startsWith(ADAHANDLE_POLICY_ID))
                .map(foo -> {
                    String policyId = foo.substring(0, POLICY_ID_LENGTH);
                    String assetNameHex = foo.substring(POLICY_ID_LENGTH);
                    String assetName = new String(HexUtil.decodeHexString(assetNameHex));
                    return new Tuple(policyId, assetName);
                })
                .forEach(tuple -> System.out.printf("%s %s%n", tuple._1, tuple._2));
    }

    @GetMapping("/adahandles")
    public List<String> getAdahandles(@RequestParam("base_address") String baseAddress) throws ApiException {
        AddressService addressService = blockfrostService.getAddressService();
        Result<AddressContent> addressInfo = addressService.getAddressInfo(baseAddress);
        List<String> adahandles = addressInfo
                .getValue()
                .getAmount()
                .stream()
                .map(TxContentOutputAmount::getUnit)
                .filter(unit -> unit.startsWith(ADAHANDLE_POLICY_ID))
                .map(unit -> {
//                    String policyId = foo.substring(0, POLICY_ID_LENGTH);
                    String assetNameHex = unit.substring(POLICY_ID_LENGTH);
                    String assetName = new String(HexUtil.decodeHexString(assetNameHex));
                    System.out.println(assetName);
                    return assetName;
                })
                .collect(Collectors.toList());
        return adahandles;
    }

}
