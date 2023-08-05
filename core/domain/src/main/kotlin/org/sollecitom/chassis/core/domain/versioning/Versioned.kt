package org.sollecitom.chassis.core.domain.versioning

interface Versioned<VERSION : Comparable<VERSION>> {

    val version: VERSION
}