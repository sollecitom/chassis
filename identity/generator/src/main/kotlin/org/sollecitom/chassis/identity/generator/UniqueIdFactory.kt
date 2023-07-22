package org.sollecitom.chassis.identity.generator

import org.sollecitom.chassis.identity.domain.Id
import org.sollecitom.chassis.identity.generator.ulid.ULIDFactory

interface UniqueIdFactory {

    val ulid: ULIDFactory

    companion object
}

val Id.Companion.Factory: UniqueIdFactory.Companion get() = UniqueIdFactory