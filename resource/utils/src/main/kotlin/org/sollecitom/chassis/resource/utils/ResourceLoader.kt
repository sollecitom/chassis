package org.sollecitom.chassis.resource.utils

import com.google.common.io.Resources
import java.io.InputStream

object ResourceLoader {

    fun resolveAbsolutePath(resourceName: String): String = Resources.getResource(resourceName).path

    fun openAsStream(resourceName: String): InputStream = Resources.getResource(resourceName).openStream()

    fun readAsText(resourceName: String): String = Resources.getResource(resourceName).readText()
}
