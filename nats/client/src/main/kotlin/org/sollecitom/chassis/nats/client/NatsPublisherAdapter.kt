package org.sollecitom.chassis.nats.client

import io.nats.client.Message
import io.nats.client.Nats
import io.nats.client.Options
import java.util.concurrent.Executors

private class NatsPublisherAdapter(options: Options) : NatsPublisher {

    private val executor = Executors.newVirtualThreadPerTaskExecutor()
    private val connection by lazy { Nats.connect(Options.Builder(options).executor(executor).build()) }

    override suspend fun publish(message: Message) = connection.publish(message)

    override suspend fun stop() {
        connection.close()
        executor.close()
    }
}

fun NatsPublisher.Companion.create(options: Options): NatsPublisher = NatsPublisherAdapter(options)