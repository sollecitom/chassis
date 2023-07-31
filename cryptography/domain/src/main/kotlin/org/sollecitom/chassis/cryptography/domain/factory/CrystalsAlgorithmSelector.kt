package org.sollecitom.chassis.cryptography.domain.factory

import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberAlgorithmOperationSelector

interface CrystalsAlgorithmSelector {

    val kyber: KyberAlgorithmOperationSelector
}