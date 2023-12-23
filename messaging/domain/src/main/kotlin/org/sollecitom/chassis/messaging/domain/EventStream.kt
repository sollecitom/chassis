package org.sollecitom.chassis.messaging.domain

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.messaging.domain.Topic

interface EventStream {

    val topic: Topic

    fun messageKeyForEvent(event: Event): String

    fun messagePropertiesForEvent(event: Event): Map<String, String>
}