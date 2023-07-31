package org.sollecitom.chassis.cryptography.domain.factory

interface CryptographyCategorySelector {

    val asymmetric: AsymmetricAlgorithmFamilySelector
}