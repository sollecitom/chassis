package org.sollecitom.chassis.ddd.test.utils

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher

class InMemoryCommandProcessor<in COMMAND : Command<RESULT, ACCESS>, out RESULT : Any, out ACCESS : Access>(private val process: suspend context(InvocationContext<ACCESS>)(CommandWasReceived<COMMAND>) -> RESULT) : ReceivedCommandPublisher<COMMAND, ACCESS>, CommandResultSubscriber<COMMAND, RESULT, ACCESS> {

    private val results = mutableMapOf<CommandWasReceived<COMMAND>, CompletableDeferred<RESULT>>()

    context(InvocationContext<ACCESS>)
    override suspend fun publish(event: CommandWasReceived<COMMAND>) {

        val result = process(this@InvocationContext, event)
        results.remove(event)?.complete(result)
    }

    context(InvocationContext<ACCESS>)
    override fun resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT> {

        val result = CompletableDeferred<RESULT>()
        results[event] = result
        return result
    }
}