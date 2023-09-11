package org.sollecitom.chassis.ddd.event.stream.pulsar

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.future.await
import kotlinx.coroutines.test.runTest
import org.apache.pulsar.client.api.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.stream.EventStream
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class PulsarEventFrameworkTests : CoreDataGenerator by CoreDataGenerator.testProvider {
//private class PulsarEventFrameworkTests : EventStreamTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    private val timeout = 10.seconds
    private val pulsar = newPulsarContainer()
    private val pulsarClient by lazy { pulsar.client() }
    private val pulsarAdmin by lazy { pulsar.admin() }
//    override fun eventStream() = EventStream.Mutable.pulsar()

    @BeforeAll
    fun beforeAll() {

        pulsar.start()
    }

    @AfterAll
    fun afterAll() {

        pulsar.stop()
    }

    @Test
    fun `the container starts`() = runTest(timeout = timeout) {

        val key = "key"
        val value = "value"
        val topic = "topic"
        val subscriptionName = "a-subscription"
        val producer = pulsarClient.newProducer(Schema.STRING).topic(topic).create()
        val consumer = pulsarClient.newConsumer(Schema.STRING).topic(topic).subscriptionName(subscriptionName).subscribe()

        val messageId = producer.newMessage().key(key).value(value).sendSuspending()

        val message = consumer.receiveSuspending()

        assertThat(message.id).isEqualTo(messageId)
        assertThat(message.key).isEqualTo(key)
        assertThat(message.value).isEqualTo(value)
    }
}

suspend fun TypedMessageBuilder<*>.sendSuspending(): MessageIdAdv = sendAsync().await() as MessageIdAdv

suspend fun <VALUE> Consumer<VALUE>.receiveSuspending(): Message<VALUE> = receiveAsync().await()

val Message<*>.id: MessageIdAdv get() = messageId as MessageIdAdv

fun EventStream.Mutable.Companion.pulsar(): EventStream.Mutable {

    TODO("implement")
}