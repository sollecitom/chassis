package org.sollecitom.chassis.correlation.core.domain.access.authorization

@JvmInline
value class Roles(val values: Set<Role>): Set<Role> by values {

    companion object
}