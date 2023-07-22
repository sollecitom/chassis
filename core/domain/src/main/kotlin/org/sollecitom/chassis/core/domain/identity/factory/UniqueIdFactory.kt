package org.sollecitom.chassis.core.domain.identity.factory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.ulid.ULIDFactory

interface UniqueIdFactory {

    val ulid: ULIDFactory

    companion object
}

val Id.Companion.Factory: UniqueIdFactory.Companion get() = UniqueIdFactory