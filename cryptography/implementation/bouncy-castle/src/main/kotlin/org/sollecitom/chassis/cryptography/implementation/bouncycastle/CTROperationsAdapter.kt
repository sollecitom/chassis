package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.sollecitom.chassis.cryptography.domain.symmetric.EncryptedData
import org.sollecitom.chassis.cryptography.domain.symmetric.EncryptionMode
import java.security.SecureRandom
import javax.crypto.SecretKey

private class CTROperationsAdapter(private val key: SecretKey, private val random: SecureRandom) : EncryptionMode.CTR.Operations {

    override fun encryptWithRandomIV(bytes: ByteArray) = encrypt(bytes = bytes, iv = newIv())

    override fun encrypt(bytes: ByteArray, iv: ByteArray): EncryptedData<EncryptionMode.CTR.Metadata> {

        val (_, encrypted) = ctrEncrypt(key, iv, bytes)
        return EncryptedData(content = encrypted, EncryptionMode.CTR.Metadata(iv = iv))
    }

    override fun decrypt(bytes: ByteArray, iv: ByteArray): ByteArray = ctrDecrypt(key = key, iv = iv, cipherText = bytes)

    private fun newIv() = random.generateSeed(RANDOM_IV_LENGTH)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CTROperationsAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()

    companion object {
        private const val RANDOM_IV_LENGTH = 16
    }
}

fun EncryptionMode.CTR.Operations.Companion.create(key: SecretKey, random: SecureRandom): EncryptionMode.CTR.Operations = CTROperationsAdapter(key, random)