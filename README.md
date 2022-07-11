# Cardano Web3.0 Auth w/ Java

This project is a fully working Web 3.0 Authentication Powered By Cardano Wallets.

In the traditional Web 2.0 world, usually a user would sign up on a website using one of:

* email + password
* Single Sign-On

I've seen myself using different email addressed depending on the level of trust of the website I wanted signup on.

* I would use my traditional email if I trusted the platform
* I would re-use a less critical email for most of other platforms
* create a brand new email if I was overly skeptic about legitimacy of a website (or wanted to protect my identity)

The goal of this proposal is to create a blockchain friendly authentication approach that could cater for the example above, and possibly more.
I individuated three types of sign up

1. anonymous
2. proof of owning a certain amount of a token (either $ada or Cardano native asset eg. $hosky)
3. proof of owning a specific NFT (it's kinda like point 2)

## Anonymous

A solution to this would be simply create a new wallet and use it to authenticate on a website. Wallet is empty (and should be kept so!) so this would guarantee maximum level of 
privacy.

## Proof of owning a certain amount of a token

Should $hosky community required to vote on a proposal, they could use this login mechanism to verify how much token a wallet holds and implement
a simple but effective voting mechanism. 

Or someone could very well flex their 5m $ada, by simply signing up as demonstrated in this project.

## Proof of owning a specific NFT

From a technical PoV is exactly like point 2, but it opens up at interesting scenarios:

* a website whose secured content can be accessed by owners of NFTs of a specific collection (eg spacebuds, chilled kongs)
* adahandle based login 

## Compatibility 

While the code in this project is developed using Java, because we leveraged official and accepted Cardano [CIPs](https://cips.cardano.org/),
the features implemented in this project can be implemented using any other language or framework, that implements such CIPs (Eg. cardano serialization lib for js projects.)

# Approach used

Demonstrate wallet ownership, allows external systems to verify that:

* a wallet contains or has contained a certain amount of a specific token
* a wallet has minted/burned a certain amount of a specific token
* transferred some token or interacted w/ a smart contract

## Cardano standards

This project leverages [CIP8](https://cips.cardano.org/cips/cip8) and [CIP30](https://cips.cardano.org/cips/cip30/) to authenticate a wallet (i.e. verify the user **really owns a wallet**.)

## JWT for post authentication

Once the user has signed up / signed in, [JWT](https://jwt.io/) tokens are used to maintain the user authenticated.

# How to run the code

Instructions in the [HOW_TO_RUN](./HOW_TO_RUN.md) file
