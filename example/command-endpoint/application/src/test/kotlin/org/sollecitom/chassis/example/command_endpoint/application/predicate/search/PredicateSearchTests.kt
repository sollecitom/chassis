package org.sollecitom.chassis.example.command_endpoint.application.predicate.search

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.application.dispatching.CommandHandler
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher
import org.sollecitom.chassis.ddd.test.utils.hasInvocationContext
import org.sollecitom.chassis.example.command_endpoint.domain.predicate.search.EmailAddressValidator
import org.sollecitom.chassis.example.command_endpoint.domain.predicate.search.NoOp
import org.sollecitom.chassis.example.command_endpoint.domain.predicate.search.withDomainBlacklist
import org.sollecitom.chassis.example.event.domain.predicate.search.DeviceDescription
import org.sollecitom.chassis.example.event.domain.predicate.search.DeviceInformation
import org.sollecitom.chassis.example.event.domain.predicate.search.FindPredicateDevice
import org.sollecitom.chassis.example.event.domain.predicate.search.ProductCode

// TODO add input checks for description, email address, and product code in the HTTP endpoint
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
private class PredicateSearchTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `searching for a predicate device with an allowed email address`() = runTest {

        val emailAddress = "bruce@waynecorp.com".let(::EmailAddress)
        val deviceInformation = deviceInformation(description = "Some amazing device", productCode = "38BEE27")
        val command = FindPredicateDevice(emailAddress, deviceInformation)
        val invocationContext = InvocationContext.unauthenticated()
        var publishedEvent: CommandWasReceived<FindPredicateDevice>? = null
        val handler = newHandler { event ->
            publishedEvent = event
        }

        val result = with(invocationContext) { handler.process(command) }

        assertThat(result).wasAccepted()
        assertThat(publishedEvent).isNotNull().hasCommandAndContext(command, invocationContext)
    }

    @Test
    fun `searching for a predicate device with a disallowed email address`() = runTest {

        val emailAddress = "bruce@gmail.com".let(::EmailAddress)
        val deviceInformation = deviceInformation(description = "Another amazing device", productCode = "27ACD18")
        val command = FindPredicateDevice(emailAddress, deviceInformation)
        val invocationContext = InvocationContext.unauthenticated()
        var publishedEvent: CommandWasReceived<FindPredicateDevice>? = null
        val handler = newHandler(emailAddressValidator = EmailAddressValidator.withDomainBlacklist(blacklist = setOf("gmail.com"))) { event ->
            publishedEvent = event
        }

        val result = with(invocationContext) { handler.process(command) }

        assertThat(result).wasRejectedBecauseOfDisallowedEmailAddress()
        assertThat(publishedEvent).isNull()
    }

    private fun newHandler(emailAddressValidator: EmailAddressValidator = EmailAddressValidator.NoOp, publish: suspend context(InvocationContext<Access>)(CommandWasReceived<FindPredicateDevice>) -> Unit): CommandHandler<FindPredicateDevice, FindPredicateDevice.Result, Access> {

        val publisher = FunctionalPublisher(publish)
        return FindPredicateDeviceHandler(receivedCommandPublisher = publisher, emailAddressValidator = emailAddressValidator, uniqueIdGenerator = this, timeGenerator = this)
    }

    private fun deviceInformation(description: String, productCode: String? = null) = DeviceInformation(description.let(::Name).let(::DeviceDescription), productCode?.let(::ProductCode))

    private fun Assert<FindPredicateDevice.Result>.wasAccepted() = given { result -> assertThat(result).isInstanceOf<FindPredicateDevice.Result.Accepted>() }

    private fun Assert<FindPredicateDevice.Result>.wasRejectedBecauseOfDisallowedEmailAddress() = given { result -> assertThat(result).isInstanceOf<FindPredicateDevice.Result.Rejected.DisallowedEmailAddress>() }

    private class FunctionalPublisher(private val publish: suspend context(InvocationContext<Access>)(CommandWasReceived<FindPredicateDevice>) -> Unit) : ReceivedCommandPublisher<FindPredicateDevice, Access> {

        context(InvocationContext<Access>)
        override suspend fun publish(event: CommandWasReceived<FindPredicateDevice>) {

            publish(this@InvocationContext, event)
        }
    }
}

// TODO move this somewhere common
private fun <COMMAND : Command<*, *>> Assert<CommandWasReceived<COMMAND>>.hasCommandAndContext(command: COMMAND, context: InvocationContext<Access>) = given { event ->

    assertThat(event.command).isEqualTo(command)
    assertThat(event).hasInvocationContext(context)
}