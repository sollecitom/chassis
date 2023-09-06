package org.sollecitom.chassis.correlation.core.test.utils.tenancy

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant

context(CoreDataGenerator)
fun Tenant.Companion.create(id: Id = newId.internal()): Tenant = Tenant(id)