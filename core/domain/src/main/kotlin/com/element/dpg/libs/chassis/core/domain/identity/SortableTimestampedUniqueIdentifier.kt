package com.element.dpg.libs.chassis.core.domain.identity

import org.sollecitom.chassis.core.domain.traits.Timestamped

sealed interface SortableTimestampedUniqueIdentifier<SELF : SortableTimestampedUniqueIdentifier<SELF>> : Id, Comparable<SELF>, Timestamped