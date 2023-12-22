package org.sollecitom.chassis.correlation.core.test.utils.access.scope

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessContainer

context(UniqueIdGenerator)
fun AccessContainer.Companion.create(id: Id = newId.internal()): AccessContainer = AccessContainer(id)