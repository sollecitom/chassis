package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric

import org.sollecitom.chassis.cryptography.domain.symmetric.EncryptedData
import org.sollecitom.chassis.cryptography.domain.symmetric.EncryptionMode
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import javax.crypto.SecretKey

private class CTROperationsAdapter(private val key: SecretKey, private val random: SecureRandom, private val randomSeedLength: Int = com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.CTROperationsAdapter.Companion.DEFAULT_RANDOM_IV_LENGTH) : EncryptionMode.CTR.Operations {

    override fun encryptWithRandomIV(bytes: ByteArray) = encrypt(bytes = bytes, iv = newIv())

    override fun encrypt(bytes: ByteArray, iv: ByteArray): EncryptedData<EncryptionMode.CTR.Metadata> {

        val (_, encrypted) = BouncyCastleUtils.ctrEncrypt(key, iv, bytes)
        return EncryptedData(content = encrypted, EncryptionMode.CTR.Metadata(iv = iv))
    }

    override fun decrypt(bytes: ByteArray, iv: ByteArray): ByteArray = BouncyCastleUtils.ctrDecrypt(key = key, iv = iv, cipherText = bytes)

    private fun newIv() = random.generateSeed(randomSeedLength)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.CTROperationsAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()

    companion object {
        private const val DEFAULT_RANDOM_IV_LENGTH = 16
    }
}

fun EncryptionMode.CTR.Operations.Companion.create(key: SecretKey, random: SecureRandom): EncryptionMode.CTR.Operations = CTROperationsAdapter(key, random)