<template>
  <button @click="connect">Connect Nami</button>
  <button @click="sign">Login</button>
  <div>
    <div v-if="wallet != null">
      <div>Picked: {{ authMethod }}</div>

      <input
        type="radio"
        id="baseAddress"
        value="base_address"
        v-model="authMethod"
      />
      <label for="baseAddress">Base Address</label>

      <input
        type="radio"
        id="adaHandle"
        value="ada_handle"
        v-model="authMethod"
      />
      <label for="adaHandle">Ada Handle</label>

      <input
        type="radio"
        id="walletAmount"
        value="wallet_amount"
        v-model="authMethod"
        disabled
      />
      <label for="walletAmount">Wallet Amount</label>
    </div>

    <div v-if="authMethod == 'ada_handle'">
      <div>Selected: {{ adahandleSelected }}</div>

      <select v-model="adahandleSelected">
        <option v-for="handle in adahandles" :key="handle" :value="handle">
          {{ handle }}
        </option>
      </select>
    </div>
  </div>
</template>

<script>
import { Buffer } from "buffer";
import {
  Address,
  BaseAddress,
} from "@emurgo/cardano-serialization-lib-browser";

export default {
  name: "HelloWorld",
  props: {
    msg: String,
  },
  data() {
    return {
      authMethod: "base_address",
      wallet: null,
      adahandleSelected: null,
      adahandles: [],
    };
  },
  methods: {
    async connect() {
      console.log("hello");
      console.log(this.wallet);
      // console.log(cardano);
      // eslint-disable-next-line no-undef
      this.wallet = await cardano["nami"].enable();
      console.log(this.wallet);

      const addresses = await this.wallet.getUsedAddresses();

      const addressHex = Buffer.from(addresses[0], "hex");

      const address = BaseAddress.from_address(
        Address.from_bytes(addressHex)
      ).to_address();

      const baseAddress = address.to_bech32();
      console.log(address);
      console.log(baseAddress);

      const response = await fetch(
        "http://localhost:8080/auth/adahandles?base_address=" +
          baseAddress.toString(),
        {
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        }
      );
      const handles = await response.json();
      console.log(handles);
      this.adahandles = handles;
      console.log(this.adahandles);
    },
    async sign() {
      console.log("hi");

      const addresses = await this.wallet.getUsedAddresses();

      const addressHex = Buffer.from(addresses[0], "hex");

      const address = BaseAddress.from_address(
        Address.from_bytes(addressHex)
      ).to_address();

      const baseAddress = address.to_bech32();
      console.log(address);
      console.log(baseAddress);

      const foo = Buffer.from("Hello World!");
      console.log("before sign");

      const signedData = await this.wallet.signData(
        addresses[0],
        foo.toString("hex")
      );
      console.log("after sign");
      console.log(signedData);

      const res = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          message: foo.toString("hex"),
          base_address: baseAddress.toString(),
          data_signature: JSON.stringify(signedData),
        }),
      });

      console.log(res);
    },
  },
};
</script>
