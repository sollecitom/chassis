package org.sollecitom.chassis.example.service.endpoint.write.domain.user

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.ddd.domain.Entity

interface User : Entity<SortableTimestampedUniqueIdentifier<*>> {

    suspend fun submitRegistrationRequest()

    companion object
}