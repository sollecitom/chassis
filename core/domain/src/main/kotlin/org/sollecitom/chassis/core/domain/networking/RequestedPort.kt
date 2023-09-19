package org.sollecitom.chassis.core.domain.networking

@JvmInline
value class RequestedPort(val value: Int) : Comparable<Int> {

    init {
        require(value == RANDOM_AVAILABLE || value in EphemeralPort.range) { "Requested port value must be $RANDOM_AVAILABLE or within range ${Port.ephemeralRange}" }
    }

    fun isSpecified(): Boolean = value != RANDOM_AVAILABLE

    override fun compareTo(other: Int) = value.compareTo(other)

    companion object {

        const val RANDOM_AVAILABLE = 0

        val randomAvailable = RequestedPort(RANDOM_AVAILABLE)
    }
}