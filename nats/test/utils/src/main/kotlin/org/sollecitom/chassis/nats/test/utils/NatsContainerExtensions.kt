package org.sollecitom.chassis.nats.test.utils

import io.nats.client.Options
import org.sollecitom.chassis.nats.client.NatsConsumer
import org.sollecitom.chassis.nats.client.NatsPublisher
import org.sollecitom.chassis.nats.client.create
import org.sollecitom.chassis.nats.client.server

fun NatsContainer.newPublisher(customize: Options.Builder.() -> Unit = {}) = NatsPublisher.create(options = Options.builder().also(customize).server(host = host, port = clientPort).build())

fun NatsContainer.newConsumer(subjects: Set<String>, customize: Options.Builder.() -> Unit = {}) = NatsConsumer.create(options = Options.builder().also(customize).server(host = host, port = clientPort).build(), subjects)

fun NatsContainer.newConsumer(subject: String, customize: Options.Builder.() -> Unit = {}) = newConsumer(setOf(subject), customize)