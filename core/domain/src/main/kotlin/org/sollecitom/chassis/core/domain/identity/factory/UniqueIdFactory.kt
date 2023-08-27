package org.sollecitom.chassis.core.domain.identity.factory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.ulid.ULIDFactory

interface UniqueIdFactory {

    // TODO rename to internal and external?, and make ULID internal?
    val ulid: ULIDFactory
    val string: UniqueIdentifierFactory<Id>

    companion object
}

val Id.Companion.Factory: UniqueIdFactory.Companion get() = UniqueIdFactory