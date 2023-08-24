package org.sollecitom.chassis.core.domain.identity.factory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.identity.ulid.ULIDFactory

interface UniqueIdFactory {

    val ulid: ULIDFactory
    val string: UniqueIdentifierFactory<StringId>

    companion object
}

val Id.Companion.Factory: UniqueIdFactory.Companion get() = UniqueIdFactory