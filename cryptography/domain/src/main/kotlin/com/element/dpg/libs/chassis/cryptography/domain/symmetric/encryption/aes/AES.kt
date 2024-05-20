package com.element.dpg.libs.chassis.cryptography.domain.symmetric.encryption.aes

import org.sollecitom.chassis.cryptography.domain.symmetric.SecretKeyFactory
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import org.sollecitom.chassis.cryptography.domain.symmetric.encryption.EncryptionAlgorithm

object AES : EncryptionAlgorithm<AES.KeyArguments> {

    override val name = "AES"

    enum class Variant(val keyLength: Int, val algorithmName: String) {
        AES_128(128, "AES128"),
        AES_192(192, "AES192"),
        AES_256(256, "AES256"),
    }

    data class KeyArguments(val variant: Variant)
}

operator fun SecretKeyFactory<AES.KeyArguments, SymmetricKey>.invoke(variant: AES.Variant) = invoke(arguments = AES.KeyArguments(variant = variant))