package org.sollecitom.chassis.core.domain.versioning

@JvmInline
value class IntVersion(val value: Int) : Version<IntVersion> {

    init {
        require(value > 0) { "version value must be greater than zero" }
    }

    override fun compareTo(other: IntVersion) = value.compareTo(other.value)

    companion object
}