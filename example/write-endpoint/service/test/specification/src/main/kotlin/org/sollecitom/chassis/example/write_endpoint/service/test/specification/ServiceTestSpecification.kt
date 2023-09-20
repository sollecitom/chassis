package org.sollecitom.chassis.example.write_endpoint.service.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.test.runTest
import org.apache.pulsar.client.api.PulsarClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.test.utils.hasInvocationContext
import org.sollecitom.chassis.ddd.test.utils.isOriginating
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.event.serialization.json.jsonSerde
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.pulsar.json.serialization.asPulsarSchema
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.consume
import org.sollecitom.chassis.pulsar.utils.topic
import org.sollecitom.chassis.web.api.test.utils.MonitoringEndpointsTestSpecification
import org.sollecitom.chassis.web.api.test.utils.httpURLWithPath
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.withInvocationContext
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of
import org.testcontainers.containers.PulsarContainer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface ServiceTestSpecification : CoreDataGenerator, MonitoringEndpointsTestSpecification {

    val pulsar: PulsarContainer
    val pulsarClient: PulsarClient
    val topic: PulsarTopic
    override val timeout: Duration get() = 30.seconds

    @Test
    fun `submitting a register user command`() = runTest(timeout = timeout) {

        val emailAddress = "bruce@waynecorp.com".let(::EmailAddress)
        val json = JSONObject().put("email", JSONObject().put("address", emailAddress.value))
        val invocationContext = InvocationContext.unauthenticated()
        val schema = Event.jsonSerde.asPulsarSchema()
        val consumer = pulsarClient.newConsumer(schema).topic(topic).subscriptionName("a subscription").subscribe()

        val request = Request(Method.POST, service.httpURLWithPath("commands/register-user/v1")).body(json).withInvocationContext(invocationContext)

        val response = httpClient(request)
        val message = consumer.consume()

        assertThat(response.status).isEqualTo(Status.ACCEPTED)
        assertThat(message.value).isInstanceOf<UserRegistrationRequestWasSubmitted>()
        assertThat(message.value).hasInvocationContext(invocationContext)
        assertThat(message.value).isOriginating()
    }

    companion object : HttpApiDefinition {

        override val headerNames: HttpHeaderNames = HttpHeaderNames.of(companyName = "acme")
    }
}