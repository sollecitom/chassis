package org.sollecitom.chassis.cryptography.domain.algorithms.kyber

data class KyberKeyPairArguments(val variant: Variant) {

    enum class Variant(val keyLength: Int) {
        KYBER_512(512), KYBER_768(768), KYBER_1024(1024)
    }
}