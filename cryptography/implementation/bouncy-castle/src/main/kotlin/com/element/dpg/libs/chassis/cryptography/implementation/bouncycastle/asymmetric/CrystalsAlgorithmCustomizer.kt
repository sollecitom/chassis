package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.CrystalsAlgorithmSelector
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber.Kyber
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium.Dilithium
import java.security.SecureRandom

internal class CrystalsAlgorithmCustomizer(private val random: SecureRandom) : CrystalsAlgorithmSelector {

    override val kyber by lazy { Kyber.keyPairGenerationOperations(random) }
    override val dilithium by lazy { Dilithium.keyPairGenerationOperations(random) }
}