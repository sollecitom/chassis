package com.element.dpg.libs.chassis.core.domain.versioning

interface Versioned<VERSION : Comparable<VERSION>> {

    val version: VERSION
}