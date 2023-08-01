package org.sollecitom.chassis.cryptography.domain.asymmetric

import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory

interface KeyPairGenerationOperations<ARGUMENTS, PRIVATE_KEY : PrivateKey, PUBLIC_KEY : PublicKey> {

    val keyPair: KeyPairFactory<ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>
    val privateKey: PrivateKeyFactory<PRIVATE_KEY>
    val publicKey: PublicKeyFactory<PUBLIC_KEY>
}