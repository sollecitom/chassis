package org.sollecitom.chassis.correlation.core.domain.access.authentication

import org.sollecitom.chassis.correlation.core.domain.access.session.SimpleSession

data class CredentialsBasedAuthentication(override val token: Authentication.Token, override val session: SimpleSession) : TokenBasedAuthentication.SessionBased.ClientSide<SimpleSession> {

    companion object
}