package com.element.dpg.libs.chassis.core.domain.identity.factory

import com.element.dpg.libs.chassis.core.domain.identity.Id

interface UniqueIdentifierFactory<out ID : Id> {

    operator fun invoke(): ID

    operator fun invoke(value: String): ID
}