package org.sollecitom.chassis.correlation.core.domain.access.idp

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant

private data class IdentityProviderData(override val name: Name, override val tenant: Tenant) : IdentityProvider

fun IdentityProvider.Companion.create(name: Name, tenant: Tenant): IdentityProvider = IdentityProviderData(name, tenant)