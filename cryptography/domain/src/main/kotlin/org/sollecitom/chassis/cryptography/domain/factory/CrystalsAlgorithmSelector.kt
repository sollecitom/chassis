package org.sollecitom.chassis.cryptography.domain.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.dilithium.DilithiumAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.kyber.KyberAlgorithmOperationSelector

interface CrystalsAlgorithmSelector {

    val kyber: KyberAlgorithmOperationSelector
    val dilithium: DilithiumAlgorithmOperationSelector
}