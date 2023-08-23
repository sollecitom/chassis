package org.sollecitom.chassis.example.service.endpoint.write.domain.user

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.domain.traits.Identifiable
import org.sollecitom.chassis.ddd.domain.Entity

interface User : Entity<SortableTimestampedUniqueIdentifier<*>> {

    suspend fun submitRegistrationRequest()

    data class WithPendingRegistration(override val id: SortableTimestampedUniqueIdentifier<*>): Identifiable<SortableTimestampedUniqueIdentifier<*>> {

        companion object
    }

    companion object
}