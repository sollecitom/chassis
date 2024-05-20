package com.element.dpg.libs.chassis.ddd.test.utils

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher

class InMemoryCommandProcessor<in COMMAND : Command<RESULT, ACCESS>, out RESULT : Any, out ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>(private val process: suspend context(InvocationContext<ACCESS>)(CommandWasReceived<COMMAND>) -> RESULT) : ReceivedCommandPublisher<COMMAND, ACCESS>, CommandResultSubscriber {

    private val results = mutableMapOf<CommandWasReceived<*>, CompletableDeferred<Any>>()

    context(InvocationContext<ACCESS>)
    override suspend fun publish(event: CommandWasReceived<COMMAND>) {

        val result = process(this@InvocationContext, event)
        results.remove(event)?.complete(result)
    }

    context(InvocationContext<ACCESS>)
    @Suppress("UNCHECKED_CAST")
    override fun <COMMAND : Command<R, ACCESS>, R : Any, ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<R> {

        val result = CompletableDeferred<Any>()
        results[event] = result
        return result as Deferred<R> // TODO revisit this, as it's not type-safe this way
    }

    override suspend fun start() {

    }

    override suspend fun stop() {

    }
}