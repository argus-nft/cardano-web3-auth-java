package com.argusnft.services;

import com.bloxbean.cardano.client.backend.api.AddressService;
import com.bloxbean.cardano.client.backend.api.BackendService;
import com.bloxbean.cardano.client.backend.blockfrost.common.Constants;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import org.springframework.stereotype.Service;

@Service
public class BlockfrostService {

    private BackendService backendService = new BFBackendService(Constants.BLOCKFROST_MAINNET_URL, "");

    //    FeeCalculationService feeCalculationService = backendService.getFeeCalculationService();
//    TransactionHelperService transactionHelperService = backendService.getTransactionHelperService();
//    TransactionService transactionService = backendService.getTransactionService();
//    BlockService blockService = backendService.getBlockService();
//    AssetService assetService = backendService.getAssetService();
//    UtxoService utxoService = backendService.getUtxoService();
//    MetadataService metadataService = backendService.getMetadataService();
//    EpochService epochService = backendService.getEpochService();
    private AddressService addressService = backendService.getAddressService();

    public AddressService getAddressService() {
        return addressService;
    }
}
