package org.sollecitom.chassis.correlation.core.domain.access.authentication

import org.sollecitom.chassis.correlation.core.domain.access.session.FederatedSession

data class FederatedAuthentication(override val token: Authentication.Token, override val session: FederatedSession) : TokenBasedAuthentication.SessionBased.ClientSide<FederatedSession> {

    companion object
}