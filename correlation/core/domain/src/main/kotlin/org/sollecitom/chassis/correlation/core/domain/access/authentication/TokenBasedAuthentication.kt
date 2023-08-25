package org.sollecitom.chassis.correlation.core.domain.access.authentication

import org.sollecitom.chassis.correlation.core.domain.access.session.Session
import org.sollecitom.chassis.correlation.core.domain.access.session.SimpleSession

sealed interface TokenBasedAuthentication : Authentication {

    val token: Authentication.Token

    sealed interface SessionBased<SESSION : Session> : TokenBasedAuthentication {

        val session: SESSION

        sealed interface ClientSide<SESSION : Session> : SessionBased<SESSION> {

            companion object
        }

        companion object
    }

    data class Direct(override val token: Authentication.Token, override val session: SimpleSession) : SessionBased<SimpleSession> {

        companion object
    }

    companion object
}