package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.key.CryptographicKey
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKeyWithEncapsulation
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.key.CryptographicKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.symmetric.JavaAESKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PublicKey
import java.security.SecureRandom

internal data class JavaKEMPublicKeyAdapter(private val key: PublicKey, private val random: SecureRandom) : KEMPublicKey, CryptographicKey by CryptographicKeyAdapter(key) {

    override fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation {

        val rawKeyAndEncapsulation = BouncyCastleUtils.generateAESEncryptionKey(key, algorithm, random)
        return SymmetricKeyWithEncapsulation(key = JavaAESKeyAdapter(rawKeyAndEncapsulation.encoded, random), encapsulation = rawKeyAndEncapsulation.encapsulation)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaKEMPublicKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun fromBytes(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaKEMPublicKeyAdapter = BouncyCastleUtils.getPublicKeyFromEncoded(bytes, algorithm).let { JavaKEMPublicKeyAdapter(it, random) }
    }
}