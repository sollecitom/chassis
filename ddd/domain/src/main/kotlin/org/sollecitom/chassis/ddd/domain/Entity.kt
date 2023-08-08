package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.traits.Identifiable

interface Entity<ID> : Identifiable<ID> {

    val events: EntityEventStore
}