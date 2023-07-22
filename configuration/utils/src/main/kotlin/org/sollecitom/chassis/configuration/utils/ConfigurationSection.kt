package org.sollecitom.chassis.configuration.utils

abstract class ConfigurationSection(private val rootPath: String? = null) {

    private val sectionPrefix = rootPath?.let { "$it." } ?: ""

    protected fun fullPath(key: String) = "${sectionPrefix}$key"
}