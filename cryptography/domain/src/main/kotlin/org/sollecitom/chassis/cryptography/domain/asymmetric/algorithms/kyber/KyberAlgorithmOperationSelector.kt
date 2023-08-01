package org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.kyber

import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory

// TODO make this generic for KEM key pairs
// TODO merge this with Kyber
interface KyberAlgorithmOperationSelector {

    val keyPair: KeyPairFactory<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey>
    val privateKey: PrivateKeyFactory<KEMPrivateKey>
    val publicKey: PublicKeyFactory<KEMPublicKey>
}