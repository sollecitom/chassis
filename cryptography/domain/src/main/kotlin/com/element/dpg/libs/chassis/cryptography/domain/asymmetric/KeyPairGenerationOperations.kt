package com.element.dpg.libs.chassis.cryptography.domain.asymmetric

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory

interface KeyPairGenerationOperations<ARGUMENTS, PRIVATE_KEY : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey, PUBLIC_KEY : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey> {

    val keyPair: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory<ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>
    val privateKey: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory<PRIVATE_KEY>
    val publicKey: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory<PUBLIC_KEY>
}