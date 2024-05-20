package com.element.dpg.libs.chassis.cryptography.domain.asymmetric

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.dilithium.Dilithium

interface CrystalsAlgorithmSelector {

    val kyber: KeyPairGenerationOperations<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey>
    val dilithium: KeyPairGenerationOperations<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>
}