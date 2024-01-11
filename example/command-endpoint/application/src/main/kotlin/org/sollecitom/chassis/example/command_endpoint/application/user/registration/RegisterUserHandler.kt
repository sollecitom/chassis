package org.sollecitom.chassis.example.command_endpoint.application.user.registration

import kotlinx.coroutines.coroutineScope
import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.application.dispatching.CommandHandler
import org.sollecitom.chassis.ddd.domain.*

class RegisterUserHandler(private val receivedCommandPublisher: ReceivedCommandPublisher<RegisterUser, Access>, private val commandResultSubscriber: CommandResultSubscriber<RegisterUser, RegisterUser.Result, Access>, private val uniqueIdGenerator: UniqueIdGenerator, private val timeGenerator: TimeGenerator) : CommandHandler<RegisterUser, RegisterUser.Result, Access>, UniqueIdGenerator by uniqueIdGenerator, TimeGenerator by timeGenerator {

    override val commandType get() = RegisterUser.type

    context(InvocationContext<Access>)
    override suspend fun process(command: RegisterUser): RegisterUser.Result = coroutineScope {

        val event = command.wasReceived()
        val result = event.deferredResult()
        event.publish()
        result.await()
    }

    context(InvocationContext<Access>)
    private fun CommandWasReceived<RegisterUser>.deferredResult() = commandResultSubscriber.resultForCommand(this)

    context(InvocationContext<Access>)
    private suspend fun GenericCommandWasReceived<RegisterUser>.publish() = receivedCommandPublisher.publish(this)

    companion object
}

context(TimeGenerator, UniqueIdGenerator)
operator fun RegisterUserHandler.Companion.invoke(receivedCommandPublisher: ReceivedCommandPublisher<RegisterUser, Access>, commandResultSubscriber: CommandResultSubscriber<RegisterUser, RegisterUser.Result, Access>) = RegisterUserHandler(receivedCommandPublisher = receivedCommandPublisher, commandResultSubscriber = commandResultSubscriber, uniqueIdGenerator = this@UniqueIdGenerator, timeGenerator = this@TimeGenerator)