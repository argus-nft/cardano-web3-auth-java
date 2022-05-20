package com.argusnft.services;

import com.bloxbean.cardano.client.backend.api.AddressService;
import com.bloxbean.cardano.client.backend.api.BackendService;
import com.bloxbean.cardano.client.backend.blockfrost.common.Constants;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BlockfrostService {

    private final Logger logger = LoggerFactory.getLogger(BlockfrostService.class);

    private BackendService backendService;

    private AddressService addressService;

    public BlockfrostService(@Value("${blockfrost.key}") String blockfrostKey) {
        logger.info("blockfrostKey: " + blockfrostKey);
        this.backendService = new BFBackendService(Constants.BLOCKFROST_MAINNET_URL, blockfrostKey);
        this.addressService = backendService.getAddressService();
    }

    public AddressService getAddressService() {
        return addressService;
    }

}
