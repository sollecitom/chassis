package org.sollecitom.chassis.core.domain.identity

import com.github.f4b6a3.ulid.Ulid
import kotlinx.datetime.toKotlinInstant

// TODO add TSID (https://github.com/vladmihalcea/hypersistence-tsid or https://github.com/f4b6a3/tsid-creator)

class ULID internal constructor(private val delegate: Ulid) : SortableTimestampedUniqueIdentifier<ULID> {

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

        operator fun invoke(stringValue: String): ULID = ULID(Ulid.from(stringValue))

        operator fun invoke(bytesValue: ByteArray): ULID = ULID(Ulid.from(bytesValue))
    }
}

