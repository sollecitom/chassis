package org.sollecitom.chassis.pulsar.utils

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.future.await
import kotlinx.coroutines.isActive
import org.apache.pulsar.client.api.ClientBuilder
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageIdAdv
import org.apache.pulsar.client.api.TypedMessageBuilder
import java.net.URI

suspend fun TypedMessageBuilder<*>.produce(): MessageIdAdv = sendAsync().await() as MessageIdAdv

suspend fun <VALUE> Consumer<VALUE>.consume(): Message<VALUE> = receiveAsync().await()

val <VALUE> Consumer<VALUE>.messages: Flow<Message<VALUE>>
    get() = flow {
        while (currentCoroutineContext().isActive) {
            val message = consume()
            emit(message)
        }
    }

val Message<*>.id: MessageIdAdv get() = messageId as MessageIdAdv

fun ClientBuilder.brokerURI(brokerURI: URI): ClientBuilder = serviceUrl(brokerURI.toString())