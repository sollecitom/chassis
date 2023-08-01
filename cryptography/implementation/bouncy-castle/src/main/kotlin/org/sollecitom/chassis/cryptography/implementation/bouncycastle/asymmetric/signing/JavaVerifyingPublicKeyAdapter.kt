package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.JavaKeyMetadataAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PublicKey
import java.security.SecureRandom

internal data class JavaVerifyingPublicKeyAdapter(private val key: PublicKey, private val random: SecureRandom) : VerifyingPublicKey {

    override val encoded: ByteArray get() = key.encoded
    override val metadata: KeyMetadata = JavaKeyMetadataAdapter(key)

    // TODO add verifying functions

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaVerifyingPublicKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun fromBytes(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaVerifyingPublicKeyAdapter = BouncyCastleUtils.getPublicKeyFromEncoded(bytes, algorithm).let { JavaVerifyingPublicKeyAdapter(it, random) }
    }
}