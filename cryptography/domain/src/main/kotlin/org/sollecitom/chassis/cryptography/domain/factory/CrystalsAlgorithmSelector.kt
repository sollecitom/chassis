package org.sollecitom.chassis.cryptography.domain.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.dilithium.Dilithium
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.kyber.Kyber

interface CrystalsAlgorithmSelector {

    val kyber: Kyber.Operations
    val dilithium: Dilithium.Operations
}