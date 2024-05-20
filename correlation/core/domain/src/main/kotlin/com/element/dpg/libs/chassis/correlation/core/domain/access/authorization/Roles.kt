package com.element.dpg.libs.chassis.correlation.core.domain.access.authorization

@JvmInline
value class Roles(val values: Set<Role>): Set<Role> by values {

    companion object
}