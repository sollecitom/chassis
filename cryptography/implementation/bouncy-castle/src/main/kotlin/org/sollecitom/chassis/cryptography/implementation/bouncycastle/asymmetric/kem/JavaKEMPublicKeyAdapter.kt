package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKeyWithEncapsulation
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.symmetric.JavaAESKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.JavaKeyMetadataAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PublicKey
import java.security.SecureRandom

internal data class JavaKEMPublicKeyAdapter(private val key: PublicKey, private val random: SecureRandom) : KEMPublicKey {

    override val encoded: ByteArray get() = key.encoded
    override val metadata: KeyMetadata = JavaKeyMetadataAdapter(key)

    override fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation {

        val rawKeyAndEncapsulation = BouncyCastleUtils.generateAESEncryptionKey(key, metadata.algorithm, random)
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