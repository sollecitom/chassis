package org.sollecitom.chassis.correlation.core.domain.access.session

import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider

data class FederatedSession(override val id: StringId, val identityProvider: IdentityProvider) : Session {

    companion object
}