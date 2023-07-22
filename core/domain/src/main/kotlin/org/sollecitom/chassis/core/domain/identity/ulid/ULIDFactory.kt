package org.sollecitom.chassis.core.domain.identity.ulid

import org.sollecitom.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory

interface ULIDFactory : SortableTimestampedUniqueIdentifierFactory<ULID> {

    companion object
}