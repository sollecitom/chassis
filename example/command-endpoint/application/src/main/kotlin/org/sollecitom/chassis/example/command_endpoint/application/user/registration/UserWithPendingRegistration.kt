package org.sollecitom.chassis.example.command_endpoint.application.user.registration

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.traits.Identifiable

@JvmInline
value class UserWithPendingRegistration(override val id: Id) : Identifiable {

    companion object
}