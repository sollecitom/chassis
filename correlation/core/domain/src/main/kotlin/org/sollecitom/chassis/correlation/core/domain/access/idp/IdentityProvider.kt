package org.sollecitom.chassis.correlation.core.domain.access.idp

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant

data class IdentityProvider(val name: Name, val tenant: Tenant) {

    companion object
}