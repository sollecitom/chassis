package org.sollecitom.chassis.cryptography.domain.symmetric

data class SymmetricKeyWithEncapsulation(val key: SymmetricKey, val encapsulation: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SymmetricKeyWithEncapsulation

        if (key != other.key) return false
        if (!encapsulation.contentEquals(other.encapsulation)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + encapsulation.contentHashCode()
        return result
    }

    override fun toString() = "KeyWithEncapsulation(key=$key, encapsulation=${encapsulation.contentToString()})"
}