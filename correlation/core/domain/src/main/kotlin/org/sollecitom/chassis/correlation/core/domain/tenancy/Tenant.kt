package org.sollecitom.chassis.correlation.core.domain.tenancy

import org.sollecitom.chassis.core.domain.identity.Id

@JvmInline
value class Tenant(val id: Id) {

    companion object
}