package com.element.dpg.libs.chassis.nats.test.utils

import com.element.dpg.libs.chassis.nats.client.NatsConsumer
import com.element.dpg.libs.chassis.nats.client.NatsPublisher
import com.element.dpg.libs.chassis.nats.client.create
import com.element.dpg.libs.chassis.nats.client.server
import io.nats.client.Options

fun NatsContainer.newPublisher(customize: Options.Builder.() -> Unit = {}) = NatsPublisher.create(options = com.element.dpg.libs.chassis.nats.client.server(host = host, port = clientPort).build())

fun NatsContainer.newConsumer(subjects: Set<String>, customize: Options.Builder.() -> Unit = {}) = NatsConsumer.create(options = com.element.dpg.libs.chassis.nats.client.server(host = host, port = clientPort).build(), subjects)

fun NatsContainer.newConsumer(subject: String, customize: Options.Builder.() -> Unit = {}) = newConsumer(setOf(subject), customize)