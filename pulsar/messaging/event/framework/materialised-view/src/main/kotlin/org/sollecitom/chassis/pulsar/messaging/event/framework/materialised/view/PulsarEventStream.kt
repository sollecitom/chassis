package org.sollecitom.chassis.pulsar.messaging.event.framework.materialised.view

import org.apache.pulsar.client.api.Schema
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.messaging.domain.EventStream

interface PulsarEventStream : EventStream {

    // TODO consider making it generic to the base event type e.g., GenericTestEvent in the stub
    val schema: Schema<Event>
}