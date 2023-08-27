package org.sollecitom.chassis.correlation.core.domain.access.session

import org.sollecitom.chassis.core.domain.identity.Id

@JvmInline
value class SimpleSession(override val id: Id) : Session {

    companion object
}