package org.sollecitom.chassis.nats.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.nats.client.Dispatcher
import io.nats.client.Message
import io.nats.client.Nats
import io.nats.client.Options
import io.nats.client.impl.Headers
import io.nats.client.impl.NatsMessage
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.runBlocking
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
    fun beforeAll() = nats.start()

    @AfterAll
    fun afterAll() = nats.stop()

    @Test
    fun `consuming and publishing messages`() = runTest(timeout = timeout) {

        val subject = "a-subject"
        val publisher = nats.newPublisher()
        val consumer = nats.newConsumer(subject)
        val payload = "hello world"
        val headers = buildMap {
            put("header-key", buildList {
                add("headerValue1")
                add("headerValue2")
            })
        }

        val receiving = async(start = UNDISPATCHED) { consumer.messages.first() }
        val outboundMessage = NatsMessage(subject, null, Headers().put(headers), payload.toByteArray())
        publisher.publish(outboundMessage)
        val receivedMessage = receiving.await()
        publisher.stop()
        consumer.stop()

        assertThat(receivedMessage.subject).isEqualTo(subject)
        assertThat(String(receivedMessage.data)).isEqualTo(payload)
        assertThat(receivedMessage.headers.toMultiMap()).containsSameMultipleEntriesAs(headers)
    }
}

interface NatsPublisher : Stoppable {

    suspend fun publish(message: Message)

    companion object
}

interface NatsConsumer : Stoppable {

    val messages: Flow<Message>

    companion object
}

fun NatsPublisher.Companion.create(options: Options): NatsPublisher = NatsPublisherAdapter(options)

fun Options.Builder.server(host: String, port: Int) = server("nats://$host:$port")

fun Options.Builder.server(host: String, port: Port) = server("nats://$host:${port.value}")

fun NatsContainer.newPublisher(customize: Options.Builder.() -> Unit = {}) = NatsPublisher.create(options = Options.builder().also(customize).server(host = host, port = clientPort).build())

fun NatsContainer.newConsumer(subjects: Set<String>, customize: Options.Builder.() -> Unit = {}) = NatsConsumer.create(options = Options.builder().also(customize).server(host = host, port = clientPort).build(), subjects)
fun NatsContainer.newConsumer(subject: String, customize: Options.Builder.() -> Unit = {}) = newConsumer(setOf(subject), customize)

fun NatsConsumer.Companion.create(options: Options, subjects: Set<String>): NatsConsumer = NatsConsumerAdapter(options, subjects)

private class NatsConsumerAdapter(options: Options, private val subjects: Set<String>) : NatsConsumer {

    private val executor = Executors.newVirtualThreadPerTaskExecutor()
    private val connection by lazy { Nats.connect(Options.Builder(options).executor(executor).build()) }
    private lateinit var dispatcher: Dispatcher
    private val _messages = MutableSharedFlow<Message>()

    override val messages: Flow<Message>
        get() = _messages.onSubscription {

            dispatcher = connection.createDispatcher()
            subjects.onEach {
                dispatcher.subscribe(it) { message ->
                    runBlocking {
                        _messages.emit(message)
                    }
                }
            }
        }

    override suspend fun stop() {
        connection.close()
        executor.close()
    }
}

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