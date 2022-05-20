package com.argusnft.service;

import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.cip.cip8.COSESign1;
import com.bloxbean.cardano.client.util.HexUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCose {

    @Test
    void testMe() throws JsonProcessingException {
        String fo = "{\n" +
                "    \"signature\": \"845846a201276761646472657373583901f96aa92d9c18c272fdd09516f506050bad5f475f4edaab0d877597d37c06c1591f4ad1475c5af28337d0f43f5553d10d7f528882c2650b13a166686173686564f4582677656233617574682e636f6f6c776562736974652e636f6d3b31363533303432353238393033584098c08678936713e5a0ba9487d21b95234801fb82414716ad942393c152f4923f44769a7947dadaf60573d3b54ddd9f81ce3439a98e5cc4f6cc2e6bb625b12300\",\n" +
                "    \"key\": \"a4010103272006215820a8ec37d0fdab09227dd2a3d1fba119d61f2bb1e1c98abd9a04cf9e0c3c0b4fa7\"\n" +
                "}";

        DataSignature from = DataSignature.from(fo);

        DataSignature dataSignature = new DataSignature(from.signature(), from.key());

        Address address = new Address(from.address());
        String s = address.toBech32();
        System.out.println(s);

        System.out.println(from.signature());
        System.out.println(from.address());
        System.out.println(from.key());
        System.out.println(from.coseSign1());
        COSESign1 deserialize = COSESign1.deserialize(HexUtil.decodeHexString(from.signature()));
        System.out.println(deserialize.headers());
        System.out.println(deserialize.payload());
        System.out.println(new String(deserialize.payload()));
        System.out.println(deserialize);
//        System.out.println(from.coseSign1().headers());
//        System.out.println(from.coseSign1().payload());
//        System.out.println(from.coseSign1().signature());

        Assertions.assertTrue(true);

    }
}
