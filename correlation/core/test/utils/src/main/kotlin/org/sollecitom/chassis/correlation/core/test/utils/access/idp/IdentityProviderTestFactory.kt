package org.sollecitom.chassis.correlation.core.test.utils.access.idp

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create

context(WithCoreGenerators)
fun IdentityProvider.Companion.create(name: Name = "Octa".let(::Name), tenant: Tenant = Tenant.create()): IdentityProvider = IdentityProvider(name, tenant)