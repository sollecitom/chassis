package org.sollecitom.chassis.cryptography.domain.asymmetric.signing

data class Signature(val bytes: ByteArray, val metadata: Metadata) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Signature

        if (!bytes.contentEquals(other.bytes)) return false
        if (metadata != other.metadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + metadata.hashCode()
        return result
    }

    data class Metadata(val keyHash: Long, val algorithmName: String)
}