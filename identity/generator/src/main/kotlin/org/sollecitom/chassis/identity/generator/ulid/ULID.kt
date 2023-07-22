package org.sollecitom.chassis.identity.generator.ulid

import kotlinx.datetime.toKotlinInstant
import org.sollecitom.chassis.identity.domain.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.identity.generator.ulid.java.JavaUlid

class ULID internal constructor(private val delegate: JavaUlid) : SortableTimestampedUniqueIdentifier<ULID> {

    override val stringValue by lazy(delegate::toString)
    override val bytesValue: ByteArray by lazy(delegate::toBytes)
    override val timestamp by lazy(delegate.instant::toKotlinInstant)

    override fun compareTo(other: ULID) = delegate.compareTo(other.delegate)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ULID
        return delegate == other.delegate
    }

    override fun hashCode() = delegate.hashCode()

    override fun toString() = "ULID($stringValue)"

    companion object {

        operator fun invoke(stringValue: String): ULID = ULID(JavaUlid.from(stringValue))

        operator fun invoke(bytesValue: ByteArray): ULID = ULID(JavaUlid.from(bytesValue))
    }
}

