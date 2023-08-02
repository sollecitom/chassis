package org.sollecitom.chassis.cryptography.domain.asymmetric

import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.dilithium.Dilithium

interface CrystalsAlgorithmSelector {

    val kyber: KeyPairGenerationOperations<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey>
    val dilithium: KeyPairGenerationOperations<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>
}