package org.sollecitom.chassis.correlation.core.domain.access.authorization

@JvmInline
value class AuthorizationInfo(override val roles: Roles) : AuthorizationPrincipal {

    companion object
}