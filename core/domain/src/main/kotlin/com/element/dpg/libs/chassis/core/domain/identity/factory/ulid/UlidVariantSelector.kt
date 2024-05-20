package com.element.dpg.libs.chassis.core.domain.identity.factory.ulid

import com.element.dpg.libs.chassis.core.domain.identity.ULID
import com.element.dpg.libs.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory

interface UlidVariantSelector {

    val monotonic: SortableTimestampedUniqueIdentifierFactory<ULID>
}