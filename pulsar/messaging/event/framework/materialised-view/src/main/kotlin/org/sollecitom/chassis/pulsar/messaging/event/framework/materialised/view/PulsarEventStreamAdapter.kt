package org.sollecitom.chassis.pulsar.messaging.event.framework.materialised.view

import org.apache.pulsar.client.api.Schema
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.messaging.domain.EventStream

private class PulsarEventStreamAdapter(override val schema: Schema<Event>, delegate: EventStream) : EventStream by delegate, PulsarEventStream

fun EventStream.withSchema(schema: Schema<Event>): PulsarEventStream = PulsarEventStreamAdapter(schema, this)