package org.sollecitom.chassis.correlation.core.domain.tenancy

import org.sollecitom.chassis.core.domain.identity.StringId

@JvmInline
value class Tenant(val id: StringId) {

    companion object
}