package org.sollecitom.chassis.core.domain.identity.factory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.factory.ksuid.KsuidVariantSelector
import org.sollecitom.chassis.core.domain.identity.factory.tsid.TsidVariantSelector
import org.sollecitom.chassis.core.domain.identity.factory.ulid.UlidVariantSelector

interface UniqueIdFactory {

    val ulid: UlidVariantSelector
    val ksuid: KsuidVariantSelector
    val tsid: TsidVariantSelector
    val internal: SortableTimestampedUniqueIdentifierFactory<*>
    val forEntities: SortableTimestampedUniqueIdentifierFactory<*>
    val external: UniqueIdentifierFactory<Id>

    companion object
}

val Id.Companion.Factory: UniqueIdFactory.Companion get() = UniqueIdFactory