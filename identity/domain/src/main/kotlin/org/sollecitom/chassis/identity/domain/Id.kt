package org.sollecitom.chassis.identity.domain

import org.sollecitom.chassis.core.domain.traits.DataSerializable
import org.sollecitom.chassis.core.domain.traits.StringSerializable

interface Id<SELF : Id<SELF>> : StringSerializable, DataSerializable {

    override val bytesValue: ByteArray get() = stringValue.toByteArray()

    companion object
}