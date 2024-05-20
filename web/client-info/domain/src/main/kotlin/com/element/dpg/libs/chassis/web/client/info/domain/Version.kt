package com.element.dpg.libs.chassis.web.client.info.domain

import org.sollecitom.chassis.core.domain.naming.Name

sealed interface Version : Comparable<Version> {

    val value: Name

    override fun compareTo(other: Version) = value.compareTo(other.value)

    data class Simple(override val value: Name) : Version {

        companion object
    }

    data class Semantic(val major: Int, val minor: Int, val patch: Int) : Version {

        init {
            require(major >= 0)
            require(minor >= 0)
            require(patch >= 0)
        }

        override val value = "${major}$SEPARATOR${minor}$SEPARATOR${patch}".let(::Name)

        companion object {
            const val SEPARATOR = "."

            fun parse(rawValue: String): Semantic {

                val parts = rawValue.split(SEPARATOR)
                require(parts.size in 2..3) { "A semantic version should contain at least 2 dot-separated parts" }
                val major = parts[0].toIntOrNull() ?: error("The major part of a semantic version should be an integer")
                val minor = parts[1].toIntOrNull() ?: error("The minor part of a semantic version should be an integer")
                val patch = (parts.getOrNull(2) ?: "0").toIntOrNull() ?: error("The minor part of a semantic version should be an integer")
                return Semantic(major, minor, patch)
            }
        }
    }

    companion object
}

fun Version.Companion.parse(rawValue: Name): Version = try {
    Version.Semantic.parse(rawValue.value)
} catch (error: Exception) {
    Version.Simple(rawValue)
}