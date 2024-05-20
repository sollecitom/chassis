package com.element.dpg.libs.chassis.cryptography.domain.asymmetric

interface KeyPairGenerationOperations<ARGUMENTS, PRIVATE_KEY : PrivateKey, PUBLIC_KEY : PublicKey> {

    val keyPair: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory<ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>
    val privateKey: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory<PRIVATE_KEY>
    val publicKey: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory<PUBLIC_KEY>
}