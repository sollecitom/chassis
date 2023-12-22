package org.sollecitom.chassis.core.utils

import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory

interface UniqueIdGenerator {

    val newId: UniqueIdFactory
}