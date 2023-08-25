package org.sollecitom.chassis.correlation.core.test.utils.tenancy

import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant

context(WithCoreGenerators)
fun Tenant.Companion.create(id: StringId = newId.string()): Tenant = Tenant(id)