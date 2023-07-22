package org.sollecitom.chassis.identity.domain

import org.sollecitom.chassis.core.domain.traits.Timestamped

interface SortableTimestampedUniqueIdentifier<SELF : SortableTimestampedUniqueIdentifier<SELF>> : Id<SELF>, Comparable<SELF>, Timestamped