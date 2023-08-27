package org.sollecitom.chassis.correlation.core.domain.access.session

import org.sollecitom.chassis.core.domain.identity.Id

interface Session {

    val id: Id

    companion object
}