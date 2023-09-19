package org.sollecitom.chassis.example.event.domain

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.ddd.domain.Event

// TODO move to another module?
data class Published<out EVENT : Event>(val event: EVENT, val wasPersisted: Deferred<Unit>)