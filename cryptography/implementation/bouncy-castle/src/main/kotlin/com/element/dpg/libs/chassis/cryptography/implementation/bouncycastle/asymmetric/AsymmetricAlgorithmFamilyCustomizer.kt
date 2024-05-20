package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.CrystalsAlgorithmSelector
import java.security.SecureRandom

internal class AsymmetricAlgorithmFamilyCustomizer(private val random: SecureRandom) : AsymmetricAlgorithmFamilySelector {

    override val crystals: CrystalsAlgorithmSelector by lazy { CrystalsAlgorithmCustomizer(random) }
}