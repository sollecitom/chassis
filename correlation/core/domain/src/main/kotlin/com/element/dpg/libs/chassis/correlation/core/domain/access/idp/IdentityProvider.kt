package com.element.dpg.libs.chassis.correlation.core.domain.access.idp

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant

data class IdentityProvider(val name: Name, val customer: Customer, val tenant: Tenant) {

    companion object
}