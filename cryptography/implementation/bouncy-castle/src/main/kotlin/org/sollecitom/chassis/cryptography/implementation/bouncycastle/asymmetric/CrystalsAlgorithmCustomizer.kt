package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric

import org.sollecitom.chassis.cryptography.domain.algorithms.dilithium.DilithiumAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.factory.CrystalsAlgorithmSelector
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber.KyberAlgorithmOperationCustomizer
import java.security.SecureRandom

internal class CrystalsAlgorithmCustomizer(private val random: SecureRandom) : CrystalsAlgorithmSelector {

    override val kyber: KyberAlgorithmOperationSelector by lazy { KyberAlgorithmOperationCustomizer(random) }
    override val dilithium: DilithiumAlgorithmOperationSelector
        get() = TODO("Not yet implemented")
}