package org.sollecitom.chassis.core.domain.networking

@JvmInline
value class SpecifiedPort(val value: Int) : Comparable<Int> {

    init {
        require(value == unspecifiedValue || value in EphemeralPort.range) { "value must be $unspecifiedValue or within range ${Port.ephemeralRange}" }
    }

    fun isSpecified(): Boolean = value != unspecifiedValue

    override fun compareTo(other: Int) = value.compareTo(other)

    companion object {

        const val unspecifiedValue = 0
    }
}