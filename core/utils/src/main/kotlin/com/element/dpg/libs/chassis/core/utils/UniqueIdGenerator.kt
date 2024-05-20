package com.element.dpg.libs.chassis.core.utils

import com.element.dpg.libs.chassis.core.domain.identity.factory.UniqueIdFactory

interface UniqueIdGenerator {

    val newId: UniqueIdFactory
}