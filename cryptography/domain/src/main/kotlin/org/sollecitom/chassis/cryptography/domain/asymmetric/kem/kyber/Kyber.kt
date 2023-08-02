package org.sollecitom.chassis.cryptography.domain.asymmetric.kem.kyber

import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMAlgorithm
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey

object Kyber : KEMAlgorithm<Kyber.KeyPairArguments> {

    override val name = "KYBER"

    enum class Variant(val keyLength: Int, val algorithmName: String) {
        KYBER_512(512, "KYBER512"),
        KYBER_768(768, "KYBER768"),
        KYBER_1024(1024, "KYBER1024"),
        KYBER_512_AES(512, "KYBER512-AES"),
        KYBER_768_AES(768, "KYBER768-AES"),
        KYBER_1024_AES(1024, "KYBER1024-AES"),
    }

    data class KeyPairArguments(val variant: Variant)
}

operator fun KeyPairFactory<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey>.invoke(variant: Kyber.Variant) = invoke(arguments = Kyber.KeyPairArguments(variant = variant))