package com.element.dpg.libs.chassis.resource.utils

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.BufferedReader
import java.nio.file.Paths

@TestInstance(PER_CLASS)
private class ResourceLoaderTest {

    private val fileName = "TestResource.txt"
    private val fileContent = "The test content"

    @Test
    fun `resolve absolute path`() {

        val resourceDirectory = Paths.get("build", "resources", "test")

        assertThat(ResourceLoader.resolveAbsolutePath(fileName)).isEqualTo("${resourceDirectory.toFile().absolutePath}/$fileName")
    }

    @Test
    fun `open as inputStream`() {

        var content: String
        BufferedReader(ResourceLoader.openAsStream(fileName).reader()).use { r ->
            content = r.readText()
        }

        assertThat(content).isEqualTo(fileContent)
    }

    @Test
    fun `read as text`() {

        assertThat(ResourceLoader.readAsText(fileName)).isEqualTo(fileContent)
    }

    @Test
    fun `fails to open stream when resource doesn't exist`() {

        val result = runCatching { ResourceLoader.openAsStream("FileThatDoesNotExist") }

        assertThat(result).isFailure().hasMessage("resource FileThatDoesNotExist not found.")
    }
}
