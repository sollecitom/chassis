package com.element.dpg.libs.chassis.cryptography.domain.asymmetric

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.dilithium.Dilithium

interface CrystalsAlgorithmSelector {

    val kyber: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations<com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.KeyPairArguments, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey>
    val dilithium: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>
}