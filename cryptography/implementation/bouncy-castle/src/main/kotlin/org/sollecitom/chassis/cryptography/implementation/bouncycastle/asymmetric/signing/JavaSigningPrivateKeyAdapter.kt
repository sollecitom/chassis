package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.JavaKeyMetadataAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import java.security.PrivateKey as JavaPrivateKey

internal data class JavaSigningPrivateKeyAdapter(private val key: JavaPrivateKey, private val random: SecureRandom) : SigningPrivateKey {

    override val encoded: ByteArray get() = key.encoded
    override val metadata: KeyMetadata = JavaKeyMetadataAdapter(key)

    // TODO add signing functions

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaSigningPrivateKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun fromBytes(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaSigningPrivateKeyAdapter = BouncyCastleUtils.getPrivateKeyFromEncoded(bytes, algorithm).let { JavaSigningPrivateKeyAdapter(it, random) }
    }
}