package org.sollecitom.chassis.nats.test.utils

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.nats.client.Message
import io.nats.client.Nats
import io.nats.client.Options
import io.nats.client.impl.Headers
import io.nats.client.impl.NatsMessage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.test.utils.assertions.containsSameMultipleEntriesAs
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class NatsContainerExampleTests {

    private val timeout = 10.seconds
    private val nats = NatsContainer()

    @BeforeAll
    fun beforeAll() {
        nats.start()
    }

    @AfterAll
    fun afterAll() {
        nats.stop()
    }

    @Test
    fun `consuming and publishing messages with the raw client`() = runTest(timeout = timeout) {

        val options = Options.builder().server("nats://${nats.host}:${nats.clientPort.value}").build()
        val publishingConnection = Nats.connect(options) // TODO use connectAsynchronously with a connectionListener in the builder
        val consumingConnection = Nats.connect(options) // TODO use connectAsynchronously with a connectionListener in the builder
        val subject = "a-subject"
        val payload = "hello world"
        val headers = buildMap {
            put("header-key", buildList {
                add("headerValue1")
                add("headerValue2")
            })
        }

        val receivedMessages = mutableListOf<Message>()
        val receiving = CompletableDeferred<Unit>()
        val handler = consumingConnection.createDispatcher { message ->

            receivedMessages += message
            receiving.complete(Unit)
        }.subscribe(subject)
        publishingConnection.publish(NatsMessage(subject, null, Headers().put(headers), payload.toByteArray()))
        receiving.await()
        handler.unsubscribe(subject)
        consumingConnection.close()
        publishingConnection.close()

        assertThat(receivedMessages).hasSize(1)
        val receivedMessage = receivedMessages.single()
        assertThat(receivedMessage.subject).isEqualTo(subject)
        assertThat(String(receivedMessage.data)).isEqualTo(payload)
        assertThat(receivedMessage.headers.toMultiMap()).containsSameMultipleEntriesAs(headers)
    }

    @Test
    fun `consuming and publishing messages`() = runTest(timeout = timeout) {

        val options = Options.builder().server(host = nats.host, port = nats.clientPort).build()
        val publisher = nats.newPublisher()
        val consumingConnection = Nats.connect(options) // TODO use connectAsynchronously with a connectionListener in the builder
        val subject = "another-subject"
        val payload = "hello world again"
        val headers = buildMap {
            put("header-key", buildList {
                add("headerValue1")
                add("headerValue2")
            })
        }

        val receivedMessages = mutableListOf<Message>()
        val receiving = CompletableDeferred<Unit>()
        val handler = consumingConnection.createDispatcher { message ->

            receivedMessages += message
            receiving.complete(Unit)
        }.subscribe(subject)
        publisher.publish(NatsMessage(subject, null, Headers().put(headers), payload.toByteArray()))
        receiving.await()
        handler.unsubscribe(subject)
        consumingConnection.close()
        publisher.stop()

        assertThat(receivedMessages).hasSize(1)
        val receivedMessage = receivedMessages.single()
        assertThat(receivedMessage.subject).isEqualTo(subject)
        assertThat(String(receivedMessage.data)).isEqualTo(payload)
        assertThat(receivedMessage.headers.toMultiMap()).containsSameMultipleEntriesAs(headers)
    }
}

interface NatsPublisher : Stoppable {

    suspend fun publish(message: Message)

    companion object
}

fun NatsPublisher.Companion.create(options: Options): NatsPublisher = NatsPublisherAdapter(options)

fun Options.Builder.server(host: String, port: Int) = server("nats://$host:$port")

fun Options.Builder.server(host: String, port: Port) = server("nats://$host:${port.value}")

fun NatsContainer.newPublisher(customize: Options.Builder.() -> Unit = {}) = NatsPublisher.create(options = Options.builder().also(customize).server(host = host, port = clientPort).build())

private class NatsPublisherAdapter(options: Options) : NatsPublisher {

    private val executor = Executors.newVirtualThreadPerTaskExecutor()
    private val connection by lazy { Nats.connect(Options.Builder(options).executor(executor).build()) }

    override suspend fun publish(message: Message) = connection.publish(message)

    override suspend fun stop() {
        connection.close()
        executor.close()
    }
}

private fun Headers.toMultiMap(): Map<String, List<String>> = entrySet().associate { it.key to it.value }