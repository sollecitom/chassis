package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.domain.Command

interface ApplicationCommand<out RESULT, out ACCESS : Access<SortableTimestampedUniqueIdentifier<*>>> : Command {

    companion object
}