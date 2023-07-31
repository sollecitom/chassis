package org.sollecitom.chassis.cryptography.domain.factory

interface CryptographyOperationCategorySelector {

    val asymmetric: AsymmetricAlgorithmFamilySelector
}