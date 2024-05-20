package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber

object Kyber : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMAlgorithm<Kyber.KeyPairArguments> {

    override val name = "KYBER"

    enum class Variant(val keyLength: Int, val algorithmName: String) {
        KYBER_512(512, "KYBER512"),
        KYBER_768(768, "KYBER768"),
        KYBER_1024(1024, "KYBER1024")
    }

    data class KeyPairArguments(val variant: Variant)
}

operator fun com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory<Kyber.KeyPairArguments, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey>.invoke(variant: Kyber.Variant) = invoke(arguments = Kyber.KeyPairArguments(variant = variant))