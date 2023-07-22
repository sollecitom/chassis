package org.sollecitom.chassis.core.domain.identity

import org.sollecitom.chassis.core.domain.traits.Timestamped

interface SortableTimestampedUniqueIdentifier<SELF : SortableTimestampedUniqueIdentifier<SELF>> : Id<SELF>, Comparable<SELF>, Timestamped