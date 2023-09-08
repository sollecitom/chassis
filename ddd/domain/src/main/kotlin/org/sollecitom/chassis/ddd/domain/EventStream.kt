package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow

interface EventStream<out EVENT : Event> {

    val asFlow: Flow<EVENT>

    interface Mutable<EVENT : Event> : EventStream<EVENT> {

        suspend fun publish(event: EVENT)
    }

    companion object
}