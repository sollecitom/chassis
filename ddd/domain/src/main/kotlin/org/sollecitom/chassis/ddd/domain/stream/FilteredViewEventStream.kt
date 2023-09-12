package org.sollecitom.chassis.ddd.domain.stream

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.filterIsForEntityId

class FilteredViewEventStream(override val entityId: Id, private val stream: EventStream.Mutable) : EventStream.EntitySpecific.Mutable {

    override suspend fun publish(event: EntityEvent) {

        require(event.entityId == entityId) { "Cannot publish an event for entity ID '${event.entityId.stringValue}' on an entity-specific event stream for entity ID '${entityId.stringValue}'" }
        stream.publish(event)
    }

    override val asFlow: Flow<EntityEvent> get() = stream.asFlow.filterIsForEntityId(entityId)
}