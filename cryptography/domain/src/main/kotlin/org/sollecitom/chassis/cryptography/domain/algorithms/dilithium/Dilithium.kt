package org.sollecitom.chassis.cryptography.domain.algorithms.dilithium

object Dilithium {

    const val NAME = "DILITHIUM"

    enum class Variant(val signatureAlgorithm: String) {

        DILITHIUM_2("DILITHIUM2"),
        DILITHIUM_3("DILITHIUM3"),
        DILITHIUM_5("DILITHIUM5"),
        DILITHIUM_2_AES("DILITHIUM2-AES"),
        DILITHIUM_3_AES("DILITHIUM3-AES"),
        DILITHIUM_5_AES("DILITHIUM5-AES"),
    }

    data class KeyPairArguments(val variant: Variant)
}