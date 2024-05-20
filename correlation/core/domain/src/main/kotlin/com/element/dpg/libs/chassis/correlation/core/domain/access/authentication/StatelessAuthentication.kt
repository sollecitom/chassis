package com.element.dpg.libs.chassis.correlation.core.domain.access.authentication

data class StatelessAuthentication(override val token: Authentication.Token) : TokenBasedAuthentication {

    companion object
}