package com.element.dpg.libs.chassis.cryptography.domain.factory

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector

interface CryptographicOperations {

    val asymmetric: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
    val symmetric: SymmetricAlgorithmFamilySelector

    companion object
}