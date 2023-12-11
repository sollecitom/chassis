package org.sollecitom.chassis.core.domain.identity

import com.github.f4b6a3.ksuid.Ksuid
import com.github.f4b6a3.ulid.Ulid
import kotlinx.datetime.toKotlinInstant

class KSUID internal constructor(private val delegate: Ksuid) : SortableTimestampedUniqueIdentifier<KSUID> {

    override val stringValue by lazy(delegate::toString)
    override val bytesValue: ByteArray by lazy(delegate::toBytes)
    override val timestamp by lazy(delegate.instant::toKotlinInstant)

    override fun compareTo(other: KSUID) = delegate.compareTo(other.delegate)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as KSUID
        return delegate == other.delegate
    }

    override fun hashCode() = delegate.hashCode()

    override fun toString() = "ULID($stringValue)"

    companion object {

        operator fun invoke(stringValue: String): KSUID = KSUID(Ksuid.from(stringValue))

        operator fun invoke(bytesValue: ByteArray): KSUID = KSUID(Ksuid.from(bytesValue))
    }
}

