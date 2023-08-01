package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric

import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.dilithium.DilithiumAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.kyber.KyberAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.factory.CrystalsAlgorithmSelector
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber.KyberAlgorithmOperationCustomizer
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium.DilithiumAlgorithmOperationCustomizer
import java.security.SecureRandom

internal class CrystalsAlgorithmCustomizer(private val random: SecureRandom) : CrystalsAlgorithmSelector {

    override val kyber: KyberAlgorithmOperationSelector by lazy { KyberAlgorithmOperationCustomizer(random) }
    override val dilithium: DilithiumAlgorithmOperationSelector by lazy { DilithiumAlgorithmOperationCustomizer(random) }
}