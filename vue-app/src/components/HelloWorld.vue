<template>
  <button @click="connect">Connect Eternl</button>
  <button @click="sign">Sign</button>
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
      count: 0,
      wallet: null,
    };
  },
  methods: {
    async connect() {
      console.log("hello");
      console.log(this.wallet);
      // console.log(cardano);
      // eslint-disable-next-line no-undef
      this.wallet = await cardano["eternl"].enable();
      console.log(this.wallet);
    },
    async sign() {

      console.log('hi')

      const addresses = await this.wallet.getUsedAddresses();

      const addressHex = Buffer.from(addresses[0], "hex");

      const baseAddress = BaseAddress.from_address(Address.from_bytes(addressHex))        .to_address()        .to_bech32();
      console.log(baseAddress);

      const foo = Buffer.from('Hello World!');
      const signedData = await this.wallet.signData(baseAddress, foo.toString('hex'));

      console.log(signedData);
    },
  },
};
</script>
