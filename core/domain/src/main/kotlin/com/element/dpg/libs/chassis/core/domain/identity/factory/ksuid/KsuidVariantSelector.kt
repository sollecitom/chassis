package com.element.dpg.libs.chassis.core.domain.identity.factory.ksuid

import org.sollecitom.chassis.core.domain.identity.KSUID
import org.sollecitom.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory

interface KsuidVariantSelector {

    val monotonic: SortableTimestampedUniqueIdentifierFactory<KSUID>
    val withSubSecondPrecision: SortableTimestampedUniqueIdentifierFactory<KSUID>
}