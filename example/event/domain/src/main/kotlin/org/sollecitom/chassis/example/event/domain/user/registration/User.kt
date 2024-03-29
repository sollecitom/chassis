package org.sollecitom.chassis.example.event.domain.user.registration

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.traits.Identifiable

@JvmInline
value class User(override val id: Id) : Identifiable {

    companion object
}