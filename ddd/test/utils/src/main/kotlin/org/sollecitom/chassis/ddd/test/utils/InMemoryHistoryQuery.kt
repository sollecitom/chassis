package org.sollecitom.chassis.ddd.test.utils

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import kotlin.reflect.KClass

interface InMemoryHistoryQuery<QUERY : EventStore.History.Query<EVENT>, EVENT : Event> {

    val eventType: KClass<EVENT>

    operator fun invoke(event: EVENT): Boolean
}