package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

interface Application {

    suspend operator fun <RESULT, ACCESS : Access<SortableTimestampedUniqueIdentifier<*>>> invoke(command: ApplicationCommand<RESULT, ACCESS>, context: InvocationContext<ACCESS>): RESULT

    companion object
}