package com.element.dpg.libs.chassis.core.domain.networking

@JvmInline
value class Port(val value: Int) : Comparable<Port> {

    init {
        require(value in admissibleRange) { "value must be within range $admissibleRange" }
    }

    fun isEphemeral(): Boolean = value in ephemeralRange

    override fun compareTo(other: Port) = value.compareTo(other.value)

    companion object {

        val ephemeralRange = 1024..65535
        val admissibleRange = 1..65535
    }
}