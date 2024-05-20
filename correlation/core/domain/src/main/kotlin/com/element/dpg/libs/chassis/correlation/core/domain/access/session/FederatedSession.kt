package com.element.dpg.libs.chassis.correlation.core.domain.access.session

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider

data class FederatedSession(override val id: Id, val identityProvider: IdentityProvider) : Session {

    companion object
}