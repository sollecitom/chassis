package com.element.dpg.libs.chassis.resource.utils

import com.google.common.io.Resources
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.toPath

object ResourceLoader {

    fun resolveAbsolutePath(resourceName: String): String = resolvePath(resourceName).absolutePathString()

    fun openAsStream(resourceName: String): InputStream = resolve(resourceName).openStream()

    fun resolvePath(resourceName: String): Path = resolve(resourceName).toURI().toPath()

    fun readAsText(resourceName: String): String = resolve(resourceName).readText()

    fun resolve(resourceName: String): URL = try {
        URI.create(resourceName).toURL()
    } catch (error: IllegalArgumentException) {
        Resources.getResource(resourceName)
    }
}
