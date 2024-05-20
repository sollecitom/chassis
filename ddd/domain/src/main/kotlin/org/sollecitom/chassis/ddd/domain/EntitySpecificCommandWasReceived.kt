package org.sollecitom.chassis.ddd.domain

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.TimeGenerator
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

class EntitySpecificCommandWasReceived<out COMMAND : EntityCommand<*, *>>(command: COMMAND, id: Id, timestamp: Instant, context: Event.Context) : CommandWasReceived<COMMAND>(command, id, timestamp, context), EntityEvent {

    override val entityId get() = command.entityId
    override val entityType get() = command.entityType

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntitySpecificCommandWasReceived<*>

        return id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString() = "EntitySpecificCommandWasReceived(command=$command, id=$id, timestamp=$timestamp, context=$context)"
}

context(InvocationContext<Access>, UniqueIdGenerator, TimeGenerator)
fun <COMMAND : EntityCommand<*, *>> COMMAND.wasReceived(): EntitySpecificCommandWasReceived<COMMAND> {

    val id = newId.forEntities()
    val timestamp = clock.now()
    val context = Event.Context(invocation = this@InvocationContext)
    return EntitySpecificCommandWasReceived(this, id, timestamp, context)
}