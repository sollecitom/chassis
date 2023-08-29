package org.sollecitom.chassis.correlation.core.domain.access.authorization

interface AuthorizationPrincipal {

    val roles: Roles

    companion object
}