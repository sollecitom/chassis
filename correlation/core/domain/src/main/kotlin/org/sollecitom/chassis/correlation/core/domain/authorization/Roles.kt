package org.sollecitom.chassis.correlation.core.domain.authorization

@JvmInline
value class Roles(val values: Set<Role>): Set<Role> by values