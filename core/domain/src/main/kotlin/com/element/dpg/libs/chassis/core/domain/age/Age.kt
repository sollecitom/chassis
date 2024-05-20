package com.element.dpg.libs.chassis.core.domain.age

@JvmInline
value class Age(val value: Int) : Comparable<com.element.dpg.libs.chassis.core.domain.age.Age> {

    init {
        require(value >= 0) { "value cannot be negative" }
    }

    override fun compareTo(other: com.element.dpg.libs.chassis.core.domain.age.Age) = value.compareTo(other.value)

    companion object
}