package com.element.dpg.libs.chassis.ddd.domain

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.TimeGenerator
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import kotlinx.datetime.Instant

class GenericCommandWasReceived<out COMMAND : Command<*, *>>(command: COMMAND, id: Id, timestamp: Instant, context: Event.Context) : CommandWasReceived<COMMAND>(command, id, timestamp, context) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenericCommandWasReceived<*>

        return id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString() = "GenericCommandWasReceived(command=$command, id=$id, timestamp=$timestamp, context=$context)"
}

context(InvocationContext<Access>, UniqueIdGenerator, TimeGenerator)
fun <COMMAND : Command<*, *>> COMMAND.wasReceived(): GenericCommandWasReceived<COMMAND> {

    val id = newId.forEntities()
    val timestamp = clock.now()
    val context = Event.Context(invocation = this@InvocationContext)
    return GenericCommandWasReceived(this, id, timestamp, context)
}