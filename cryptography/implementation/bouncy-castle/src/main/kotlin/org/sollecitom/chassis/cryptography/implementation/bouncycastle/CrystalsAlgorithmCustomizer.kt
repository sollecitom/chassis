package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.factory.CrystalsAlgorithmSelector
import java.security.SecureRandom

internal class CrystalsAlgorithmCustomizer(private val random: SecureRandom) : CrystalsAlgorithmSelector {

    override val kyber: KyberAlgorithmOperationSelector by lazy { KyberAlgorithmOperationCustomizer(random) }
}