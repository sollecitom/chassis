package com.element.dpg.libs.chassis.ddd.test.utils

import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.ddd.domain.Command
import com.element.dpg.libs.chassis.ddd.domain.CommandResultSubscriber
import com.element.dpg.libs.chassis.ddd.domain.CommandWasReceived
import com.element.dpg.libs.chassis.ddd.domain.ReceivedCommandPublisher
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

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