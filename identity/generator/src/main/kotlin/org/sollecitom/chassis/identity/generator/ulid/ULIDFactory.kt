package org.sollecitom.chassis.identity.generator.ulid

import org.sollecitom.chassis.identity.generator.SortableTimestampedUniqueIdentifierFactory

interface ULIDFactory : SortableTimestampedUniqueIdentifierFactory<ULID> {

    companion object
}