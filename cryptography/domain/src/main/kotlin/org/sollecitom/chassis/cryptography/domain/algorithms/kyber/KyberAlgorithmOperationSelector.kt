package org.sollecitom.chassis.cryptography.domain.algorithms.kyber

import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory

interface KyberAlgorithmOperationSelector {

    val keyPair: KeyPairFactory<KyberKeyPairArguments>
    val privateKey: PrivateKeyFactory
    val publicKey: PublicKeyFactory
}