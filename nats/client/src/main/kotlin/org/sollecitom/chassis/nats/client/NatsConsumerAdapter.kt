package org.sollecitom.chassis.nats.client

import io.nats.client.Dispatcher
import io.nats.client.Message
import io.nats.client.Nats
import io.nats.client.Options
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

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

fun NatsConsumer.Companion.create(options: Options, subjects: Set<String>): NatsConsumer = NatsConsumerAdapter(options, subjects)