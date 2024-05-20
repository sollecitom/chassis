package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.key.CryptographicKey
import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SymmetricKey
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.key.CryptographicKeyAdapter
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.symmetric.encryption.aes.AESKeyAdapter
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import java.security.PrivateKey as JavaPrivateKey

internal data class JavaKEMPrivateKeyAdapter(private val key: JavaPrivateKey, private val random: SecureRandom) : KEMPrivateKey, CryptographicKey by CryptographicKeyAdapter(key) {

    private val keyFactory = AESKeyAdapter.Factory(random)

    override fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey {

        val rawEncodedSymmetricKey = BouncyCastleUtils.decryptEncapsulatedAESKey(key, encapsulatedKey, algorithm, random)
        return keyFactory.from(rawEncodedSymmetricKey)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaKEMPrivateKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun from(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaKEMPrivateKeyAdapter = BouncyCastleUtils.getPrivateKeyFromEncoded(bytes, algorithm).let { JavaKEMPrivateKeyAdapter(it, random) }
    }
}