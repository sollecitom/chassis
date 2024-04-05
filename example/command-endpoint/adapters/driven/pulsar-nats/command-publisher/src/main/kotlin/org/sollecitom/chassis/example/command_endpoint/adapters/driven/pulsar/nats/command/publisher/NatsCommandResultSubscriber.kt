package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import kotlinx.coroutines.*
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser
import org.sollecitom.chassis.example.event.domain.user.registration.UserWithPendingRegistration

// TODO move it somewhere shared
internal class NatsCommandResultSubscriber(private val scope: CoroutineScope = CoroutineScope(SupervisorJob())) : CommandResultSubscriber {

    context(InvocationContext<ACCESS>)
    @Suppress("UNCHECKED_CAST")
    override fun <COMMAND : Command<RESULT, ACCESS>, RESULT : Any, ACCESS : Access> resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT> {

        val deferred = CompletableDeferred<RESULT>()
        //  TODO connect to NATS
        scope.launch {
            // TODO receive from NATS, parse the result, and complete the deferred
            delay(3000)
            val result = RegisterUser.Result.Accepted(user = UserWithPendingRegistration(StringId("something"))) // TODO fix this by reading the type from the message and by using a deserializer
            deferred.complete(result as RESULT)
        }
        return deferred
    }

    override suspend fun start() {

    }

    override suspend fun stop() {

    }
}