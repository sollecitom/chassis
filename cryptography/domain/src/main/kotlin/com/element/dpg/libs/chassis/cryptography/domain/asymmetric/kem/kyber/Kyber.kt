package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMAlgorithm
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey

object Kyber : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMAlgorithm<com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.KeyPairArguments> {

    override val name = "KYBER"

    enum class Variant(val keyLength: Int, val algorithmName: String) {
        KYBER_512(512, "KYBER512"),
        KYBER_768(768, "KYBER768"),
        KYBER_1024(1024, "KYBER1024")
    }

    data class KeyPairArguments(val variant: _root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.Variant)
}

operator fun com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory<_root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.KeyPairArguments, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey>.invoke(variant: _root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.Variant) = invoke(arguments = _root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.KeyPairArguments(variant = variant))