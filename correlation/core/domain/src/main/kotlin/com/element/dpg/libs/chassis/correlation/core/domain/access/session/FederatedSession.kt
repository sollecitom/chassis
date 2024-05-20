package com.element.dpg.libs.chassis.correlation.core.domain.access.session

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.correlation.core.domain.access.idp.IdentityProvider

data class FederatedSession(override val id: Id, val identityProvider: IdentityProvider) : Session {

    companion object
}