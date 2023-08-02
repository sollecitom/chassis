package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric

import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
import org.sollecitom.chassis.cryptography.domain.asymmetric.CrystalsAlgorithmSelector
import java.security.SecureRandom

internal class AsymmetricAlgorithmFamilyCustomizer(private val random: SecureRandom) : AsymmetricAlgorithmFamilySelector {

    override val crystals: CrystalsAlgorithmSelector by lazy { CrystalsAlgorithmCustomizer(random) }
}