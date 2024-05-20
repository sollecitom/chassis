package com.element.dpg.libs.chassis.core.domain.identity.factory.ulid

import org.sollecitom.chassis.core.domain.identity.ULID
import org.sollecitom.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory

interface UlidVariantSelector {

    val monotonic: SortableTimestampedUniqueIdentifierFactory<ULID>
}