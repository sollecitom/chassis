package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.core.domain.versioning.Versioned

interface Happening : Versioned<IntVersion> {

    val type: Type
    override val version: IntVersion get() = type.version

    companion object

    interface Type : Versioned<IntVersion> {

        val name: Name

        companion object
    }
}