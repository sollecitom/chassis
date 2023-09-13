package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.core.domain.versioning.Versioned

interface Happening : Versioned<IntVersion> {

    val type: Type
    override val version: IntVersion get() = type.version

    data class Type(val name: Name, override val version: IntVersion) : Versioned<IntVersion> {

        companion object
    }

    companion object
}