package org.sollecitom.chassis.cryptography.implementation.bouncycastle.symmetric

import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import org.sollecitom.chassis.cryptography.domain.symmetric.EncryptionMode
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.JavaKeyMetadataAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.create
import java.security.SecureRandom
import javax.crypto.spec.SecretKeySpec

internal data class JavaAESKeyAdapter(override val encoded: ByteArray, private val random: SecureRandom) : SymmetricKey {

    private val keySpec = SecretKeySpec(encoded, ALGORITHM)
    override val metadata: KeyMetadata = JavaKeyMetadataAdapter(keySpec)
    override val ctr: EncryptionMode.CTR.Operations by lazy { EncryptionMode.CTR.Operations.create(keySpec, random) }

    init {
        require(metadata.algorithm == ALGORITHM) { "Key algorithm must be $ALGORITHM" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaAESKeyAdapter

        return encoded.contentEquals(other.encoded)
    }

    override fun hashCode() = encoded.contentHashCode()

    override fun toString() = "JavaAESKeyAdapter(encoded=${encoded.contentToString()}, keySpec=${keySpec})"

    companion object {
        private const val ALGORITHM = "AES"
    }
}