package com.element.dpg.libs.chassis.core.domain.quantity

@JvmInline
value class Count(val value: Int) : Comparable<Count> {

    init {
        require(value >= 0) { "value cannot be negative" }
    }

    override fun compareTo(other: Count) = value.compareTo(other.value)

    companion object
}