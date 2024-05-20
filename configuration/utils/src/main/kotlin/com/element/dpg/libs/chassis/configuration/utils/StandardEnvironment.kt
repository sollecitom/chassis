package com.element.dpg.libs.chassis.configuration.utils

import org.http4k.cloudnative.env.Environment
import java.io.File

object StandardEnvironment {

    const val STANDARD_DEFAULT_CONFIGURATION_RESOURCE_NAME = "default-configuration.yml"

    val defaultYamlConfiguration: Environment by lazy { Environment.fromYamlResource(STANDARD_DEFAULT_CONFIGURATION_RESOURCE_NAME) }

    operator fun invoke(additionalExternalConfigFiles: List<File> = emptyList(), defaultConfiguration: Environment = Environment.EMPTY): Environment {

        val standardCascadingEnvironment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides defaultConfiguration
        return Environment.fromFiles(additionalExternalConfigFiles) overrides standardCascadingEnvironment
    }
}