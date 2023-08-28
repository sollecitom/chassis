package org.sollecitom.chassis.core.domain.identity.factory

import org.sollecitom.chassis.core.domain.identity.Id

interface UniqueIdFactory {

    val internal: SortableTimestampedUniqueIdentifierFactory<*>
    val external: UniqueIdentifierFactory<Id>

    companion object
}

val Id.Companion.Factory: UniqueIdFactory.Companion get() = UniqueIdFactory