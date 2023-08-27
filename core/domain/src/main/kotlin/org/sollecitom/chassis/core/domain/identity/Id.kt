package org.sollecitom.chassis.core.domain.identity

import org.sollecitom.chassis.core.domain.traits.DataSerializable
import org.sollecitom.chassis.core.domain.traits.StringSerializable

interface Id : StringSerializable, DataSerializable {

    override val bytesValue: ByteArray get() = stringValue.toByteArray()

    companion object
}

