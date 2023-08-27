package org.sollecitom.chassis.example.service.endpoint.write.application.user

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.traits.Identifiable

data class UserWithPendingRegistration(override val id: Id) : Identifiable {

    companion object
}