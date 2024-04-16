package org.sollecitom.chassis.example.command_endpoint.application.predicate.search

import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.application.dispatching.CommandHandler
import org.sollecitom.chassis.ddd.domain.GenericCommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher
import org.sollecitom.chassis.ddd.domain.wasReceived
import org.sollecitom.chassis.example.command_endpoint.domain.predicate.search.EmailAddressValidator
import org.sollecitom.chassis.example.event.domain.predicate.search.FindPredicateDevice

class FindPredicateDeviceHandler(private val receivedCommandPublisher: ReceivedCommandPublisher<FindPredicateDevice, Access>, private val emailAddressValidator: EmailAddressValidator, private val uniqueIdGenerator: UniqueIdGenerator, private val timeGenerator: TimeGenerator) : CommandHandler<FindPredicateDevice, FindPredicateDevice.Result, Access>, UniqueIdGenerator by uniqueIdGenerator, TimeGenerator by timeGenerator {

    override val commandType get() = FindPredicateDevice.type

    context(InvocationContext<Access>)
    override suspend fun process(command: FindPredicateDevice): FindPredicateDevice.Result {

        emailAddressValidator.validate(command.emailAddress).andIfFailure { return it.asDisallowed() }
        val event = command.wasReceived()
        event.publish()
        return FindPredicateDevice.Result.Accepted
    }

    context(InvocationContext<Access>)
    private suspend fun GenericCommandWasReceived<FindPredicateDevice>.publish() = receivedCommandPublisher.publish(this)

    private inline fun EmailAddressValidator.Result.andIfFailure(action: (EmailAddressValidator.Result.Failure) -> Unit) {
        if (this is EmailAddressValidator.Result.Failure) {
            action(this)
        }
    }

    private fun EmailAddressValidator.Result.Failure.asDisallowed() = FindPredicateDevice.Result.Rejected.DisallowedEmailAddress(explanation)

    companion object
}

context(TimeGenerator, UniqueIdGenerator)
operator fun FindPredicateDeviceHandler.Companion.invoke(receivedCommandPublisher: ReceivedCommandPublisher<FindPredicateDevice, Access>, emailAddressValidator: EmailAddressValidator) = FindPredicateDeviceHandler(receivedCommandPublisher = receivedCommandPublisher, emailAddressValidator = emailAddressValidator, uniqueIdGenerator = this@UniqueIdGenerator, timeGenerator = this@TimeGenerator)
