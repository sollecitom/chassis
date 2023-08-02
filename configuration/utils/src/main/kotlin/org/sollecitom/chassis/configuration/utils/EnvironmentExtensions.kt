package org.sollecitom.chassis.configuration.utils

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.fromYaml
import org.http4k.lens.BiDiLens
import org.http4k.routing.ResourceLoader
import kotlin.io.path.toPath

fun Environment.Companion.from(vararg entries: Pair<BiDiLens<Environment, *>, String>) = from(*entries.map { it.first.meta.name to it.second }.toTypedArray())

fun Environment.Companion.fromYamlResource(resourceName: String): Environment {

    val file = ResourceLoader.Classpath().load(resourceName)?.toURI()?.toPath()?.toFile() ?: error("No resource with name $resourceName was found")
    return Environment.fromYaml(file)
}