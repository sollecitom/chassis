package org.sollecitom.chassis.core.domain.networking

@JvmInline
value class EphemeralPort(val value: Int) : Comparable<Int> {

    init {
        require(value in range) { "value must be within range $range" }
    }

    override fun compareTo(other: Int) = value.compareTo(other)

    companion object {

        val range get() = Port.ephemeralRange
    }
}