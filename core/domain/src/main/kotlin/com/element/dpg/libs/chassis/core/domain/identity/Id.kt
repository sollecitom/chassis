package com.element.dpg.libs.chassis.core.domain.identity

import com.element.dpg.libs.chassis.core.domain.traits.DataSerializable
import com.element.dpg.libs.chassis.core.domain.traits.StringSerializable

sealed interface Id : StringSerializable, DataSerializable {

    override val bytesValue: ByteArray get() = stringValue.toByteArray()

    companion object
}

