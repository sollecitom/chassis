package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.JavaKeyMetadataAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.symmetric.JavaAESKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import java.security.PrivateKey as JavaPrivateKey

internal data class JavaKEMPrivateKeyAdapter(private val key: JavaPrivateKey, private val random: SecureRandom) : KEMPrivateKey {

    override val encoded: ByteArray get() = key.encoded
    override val metadata: KeyMetadata = JavaKeyMetadataAdapter(key)

    override fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey {

        val rawEncodedSymmetricKey = BouncyCastleUtils.decryptEncapsulatedAESKey(key, encapsulatedKey, metadata.algorithm, random)
        return JavaAESKeyAdapter(rawEncodedSymmetricKey, random)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaKEMPrivateKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun fromBytes(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaKEMPrivateKeyAdapter = BouncyCastleUtils.getPrivateKeyFromEncoded(bytes, algorithm).let { JavaKEMPrivateKeyAdapter(it, random) }
    }
}