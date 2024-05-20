package com.element.dpg.libs.chassis.core.domain.quantity

@JvmInline
value class NonZeroCount(val value: Int) : Comparable<NonZeroCount> {

    init {
        require(value > 0) { "value cannot be negative" }
    }

    override fun compareTo(other: NonZeroCount) = value.compareTo(other.value)

    companion object
}