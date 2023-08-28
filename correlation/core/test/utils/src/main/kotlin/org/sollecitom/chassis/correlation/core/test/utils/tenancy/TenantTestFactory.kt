package org.sollecitom.chassis.correlation.core.test.utils.tenancy

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant

context(WithCoreGenerators)
fun Tenant.Companion.create(id: Id = newId.external()): Tenant = Tenant(id)