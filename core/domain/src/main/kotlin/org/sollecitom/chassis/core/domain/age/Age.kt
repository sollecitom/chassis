package org.sollecitom.chassis.core.domain.age

@JvmInline
value class Age(val value: Int) : Comparable<Age> {

    init {
        require(value >= 0) { "value cannot be negative" }
    }

    override fun compareTo(other: Age) = value.compareTo(other.value)

    companion object
}