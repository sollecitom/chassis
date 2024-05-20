package com.element.dpg.libs.chassis.ddd.domain

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.domain.versioning.IntVersion
import com.element.dpg.libs.chassis.core.domain.versioning.Versioned

interface Happening : Versioned<IntVersion> {

    val type: Type
    override val version: IntVersion get() = type.version

    data class Type(val name: Name, override val version: IntVersion) : Versioned<IntVersion> {

        companion object
    }

    companion object
}