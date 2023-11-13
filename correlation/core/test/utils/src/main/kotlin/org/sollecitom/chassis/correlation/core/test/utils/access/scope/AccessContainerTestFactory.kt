package org.sollecitom.chassis.correlation.core.test.utils.access.scope

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessContainer
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessScope

context(CoreDataGenerator)
fun AccessContainer.Companion.create(id: Id = newId.internal()): AccessContainer = AccessContainer(id)