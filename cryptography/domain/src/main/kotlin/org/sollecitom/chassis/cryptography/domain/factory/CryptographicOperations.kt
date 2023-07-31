package org.sollecitom.chassis.cryptography.domain.factory

interface CryptographicOperations {

    val asymmetric: AsymmetricAlgorithmFamilySelector

    companion object
}