package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PrivateKey
import java.security.SecureRandom

internal data class JavaPrivateKeyAdapter(private val key: PrivateKey, private val random: SecureRandom) : org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey {

    override val encoded: ByteArray get() = key.encoded
    override val metadata: KeyMetadata = JavaKeyMetadataAdapter(key)

    override fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey {

        val rawEncodedSymmetricKey = BouncyCastleUtils.decryptEncapsulatedAESKey(key, encapsulatedKey, metadata.algorithm, random)
        return JavaAESKeyAdapter(rawEncodedSymmetricKey, random)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaPrivateKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun fromBytes(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaPrivateKeyAdapter = BouncyCastleUtils.getPrivateKeyFromEncoded(bytes, algorithm).let { JavaPrivateKeyAdapter(it, random) }
    }
}