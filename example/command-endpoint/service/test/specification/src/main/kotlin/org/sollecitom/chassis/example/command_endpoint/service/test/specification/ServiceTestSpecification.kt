package org.sollecitom.chassis.example.command_endpoint.service.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.PulsarClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.test.utils.hasInvocationContext
import org.sollecitom.chassis.ddd.test.utils.isOriginating
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser
import org.sollecitom.chassis.example.event.serialization.json.jsonSerde
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.invoke
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.pulsar.json.serialization.asPulsarSchema
import org.sollecitom.chassis.pulsar.messaing.test.utils.ensureTopicExists
import org.sollecitom.chassis.pulsar.messaing.test.utils.topics
import org.sollecitom.chassis.pulsar.utils.consume
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
    val pulsarAdmin: PulsarAdmin
    val topic: TenantAgnosticTopic
    val tenantName: Name
    val topicWithTenant: Topic get() = topic.withTenant(tenantName)
    override val timeout: Duration get() = 30.seconds

    fun specificationBeforeAll() {
        pulsar.start()
        pulsarAdmin.ensureTopicExists(topic = topicWithTenant, isAllowAutoUpdateSchema = true)
    }

    fun specificationAfterAll() {
        pulsar.stop()
    }

    @Test
    fun `submitting a register user command`() = runTest(timeout = timeout) {

        val emailAddress = "bruce@waynecorp.com".let(::EmailAddress)
        val json = JSONObject().put("email", JSONObject().put("address", emailAddress.value))
        val invocationContext = InvocationContext.unauthenticated(specifiedTargetTenant = { tenantName.asTenant() })

        val request = Request(Method.POST, service.httpURLWithPath("commands/register-user/v1")).body(json).withInvocationContext(invocationContext)
        val downstreamProcessor = StubbedRegisterUserProcessor(topicWithTenant, pulsarClient)
        val responseInFlight = async(start = UNDISPATCHED) { httpClient(request) }
        val response = responseInFlight.await()
        val message = downstreamProcessor.awaitProcessedMessage()

        assertThat(response.status).isEqualTo(Status.ACCEPTED)
        assertThat(message.value).isInstanceOf<CommandWasReceived<RegisterUser>>()
        assertThat(message.value).hasInvocationContext(invocationContext)
        assertThat(message.value).isOriginating()
        assertThat(message.value.command).isInstanceOf<RegisterUser>()
        val receivedCommand = message.value.command as RegisterUser
        assertThat(receivedCommand.emailAddress).isEqualTo(emailAddress)
    }

    private fun Name.asTenant() = Tenant(id = StringId(value))

    companion object : HttpApiDefinition {

        override val headerNames: HttpHeaderNames = HttpHeaderNames.of(companyName = "acme")
    }
}

private class StubbedRegisterUserProcessor(topic: Topic, pulsarClient: PulsarClient) {

    private val consumer = pulsarClient.newConsumer(schema).topics(topic).subscriptionName("a-subscription").subscribe()

    suspend fun awaitProcessedMessage(): Message<CommandWasReceived<Command<*, *>>> {

        val message = consumer.consume()
        // TODO publish the result to the NATS topic
        return message
    }

    companion object {
        private val schema = CommandWasReceived.jsonSerde.asPulsarSchema()
    }
}