package org.sollecitom.chassis.cryptography.domain.algorithms.dilithium

import org.sollecitom.chassis.cryptography.domain.asymmetric.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory

// TODO make this generic for signing key pairs
// TODO merge with Dilithium
interface DilithiumAlgorithmOperationSelector {

    val keyPair: KeyPairFactory<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>
    val privateKey: PrivateKeyFactory<SigningPrivateKey>
    val publicKey: PublicKeyFactory<VerifyingPublicKey>
}