package com.element.dpg.libs.chassis.cryptography.domain.symmetric

object EncryptionMode {

    object CTR {

        interface Operations {

            fun encrypt(bytes: ByteArray, iv: ByteArray): EncryptedData<Metadata>

            fun encryptWithRandomIV(bytes: ByteArray): EncryptedData<Metadata>

            fun decrypt(bytes: ByteArray, iv: ByteArray): ByteArray

            companion object
        }

        data class Metadata(val iv: ByteArray) {

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Metadata

                return iv.contentEquals(other.iv)
            }

            override fun hashCode(): Int {
                return iv.contentHashCode()
            }

            override fun toString() = "Metadata(iv=${iv.contentToString()})"
        }
    }
}

fun EncryptionMode.CTR.Operations.decrypt(data: EncryptedData<EncryptionMode.CTR.Metadata>) = decrypt(data.content, data.metadata.iv)