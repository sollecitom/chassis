package org.sollecitom.chassis.resource.utils

import com.google.common.io.Resources
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.toPath

object ResourceLoader {

    fun resolveAbsolutePath(resourceName: String): String = resolvePath(resourceName).absolutePathString()

    fun openAsStream(resourceName: String): InputStream = Resources.getResource(resourceName).openStream()

    fun resolvePath(resourceName: String): Path = Resources.getResource(resourceName).toURI().toPath()

    fun readAsText(resourceName: String): String = Resources.getResource(resourceName).readText()
}
