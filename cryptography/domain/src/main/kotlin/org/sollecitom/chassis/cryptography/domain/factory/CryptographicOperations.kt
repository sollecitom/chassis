package org.sollecitom.chassis.cryptography.domain.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector

interface CryptographicOperations {

    val asymmetric: AsymmetricAlgorithmFamilySelector
    val symmetric: SymmetricAlgorithmFamilySelector

    companion object
}