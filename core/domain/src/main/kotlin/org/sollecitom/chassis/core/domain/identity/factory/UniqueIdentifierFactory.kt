package org.sollecitom.chassis.core.domain.identity.factory

import org.sollecitom.chassis.core.domain.identity.Id

interface UniqueIdentifierFactory<ID : Id<ID>> {

    operator fun invoke(): ID

    operator fun invoke(value: String): ID
}