package org.sollecitom.chassis.example.command_endpoint.application.predicate.search

import kotlinx.coroutines.coroutineScope
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.application.dispatching.CommandHandler
import org.sollecitom.chassis.ddd.domain.GenericCommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher
import org.sollecitom.chassis.ddd.domain.wasReceived
import org.sollecitom.chassis.example.event.domain.predicate.search.FindPredicateDevice

class FindPredicateDeviceHandler(private val receivedCommandPublisher: ReceivedCommandPublisher<FindPredicateDevice, Access>, private val uniqueIdGenerator: UniqueIdGenerator, private val timeGenerator: TimeGenerator) : CommandHandler<FindPredicateDevice, FindPredicateDevice.Result, Access>, UniqueIdGenerator by uniqueIdGenerator, TimeGenerator by timeGenerator {

    override val commandType get() = FindPredicateDevice.type

    context(InvocationContext<Access>)
    override suspend fun process(command: FindPredicateDevice): FindPredicateDevice.Result = coroutineScope {

        if (command.emailAddress.isPersonal()) return@coroutineScope command.unsupportedPersonalEmailAddress()
        val event = command.wasReceived() // TODO should this stay generic? and we look up from a downstream consumer? sounds like a good idea
        event.publish()
        FindPredicateDevice.Result.Accepted
    }

    context(InvocationContext<Access>)
    private suspend fun GenericCommandWasReceived<FindPredicateDevice>.publish() = receivedCommandPublisher.publish(this)

    private fun EmailAddress.isPersonal(): Boolean {

        // TODO compare against a blacklist of email domains e.g., gmail.com?
        return false
    }

    private fun FindPredicateDevice.unsupportedPersonalEmailAddress() = FindPredicateDevice.Result.Rejected.UnsupportedPersonalEmailAddress(emailAddress)

    companion object
}