package org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.kyber

object Kyber {

    const val NAME = "KYBER"

    enum class Variant(val keyLength: Int, val algorithmName: String) {
        KYBER_512(512, "KYBER512"), KYBER_768(768, "KYBER768"), KYBER_1024(1024, "KYBER1024"),
        KYBER_512_AES(512, "KYBER512-AES"), KYBER_768_AES(768, "KYBER768-AES"), KYBER_1024_AES(1024, "KYBER1024-AES")
    }

    data class KeyPairArguments(val variant: Variant)
}