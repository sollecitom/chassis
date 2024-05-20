package com.element.dpg.libs.chassis.core.domain.identity

import kotlinx.datetime.toKotlinInstant
import io.hypersistence.tsid.TSID as Tsid

class TSID internal constructor(private val delegate: Tsid) : SortableTimestampedUniqueIdentifier<TSID> {

    override val stringValue by lazy(delegate::toString)
    override val bytesValue: ByteArray by lazy(delegate::toBytes)
    override val timestamp by lazy(delegate.instant::toKotlinInstant)

    override fun compareTo(other: TSID) = delegate.compareTo(other.delegate)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TSID
        return delegate == other.delegate
    }

    override fun hashCode() = delegate.hashCode()

    override fun toString() = "TSID($stringValue)"

    companion object {

        operator fun invoke(stringValue: String): TSID = TSID(Tsid.from(stringValue))

        operator fun invoke(bytesValue: ByteArray): TSID = TSID(Tsid.from(bytesValue))
    }
}

