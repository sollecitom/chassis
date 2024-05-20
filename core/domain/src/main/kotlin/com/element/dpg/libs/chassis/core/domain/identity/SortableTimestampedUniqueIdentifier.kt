package com.element.dpg.libs.chassis.core.domain.identity

import com.element.dpg.libs.chassis.core.domain.traits.Timestamped

sealed interface SortableTimestampedUniqueIdentifier<SELF : SortableTimestampedUniqueIdentifier<SELF>> : Id, Comparable<SELF>, Timestamped