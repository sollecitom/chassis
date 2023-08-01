package org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.dilithium

import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.Signature

// TODO make this generic for signing key pairs
// TODO merge with Dilithium
interface DilithiumAlgorithmOperationSelector {

    val keyPair: KeyPairFactory<Dilithium.KeyPairArguments, SigningPrivateKey<Unit>, VerifyingPublicKey>
    val privateKey: PrivateKeyFactory<SigningPrivateKey<Unit>>
    val publicKey: PublicKeyFactory<VerifyingPublicKey>
}