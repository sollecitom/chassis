package org.sollecitom.chassis.correlation.core.domain.authorization

@JvmInline
value class AuthorizationInfo(override val roles: Roles) : AuthorizationPrincipal