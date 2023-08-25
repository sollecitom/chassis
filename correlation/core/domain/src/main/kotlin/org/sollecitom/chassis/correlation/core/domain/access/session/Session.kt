package org.sollecitom.chassis.correlation.core.domain.access.session

import org.sollecitom.chassis.core.domain.identity.StringId

interface Session {

    val id: StringId

    companion object
}