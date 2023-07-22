package org.sollecitom.chassis.configuration.utils

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.cloudnative.env.fromYaml
import org.http4k.lens.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.io.TempDir
import org.sollecitom.chassis.lens.core.extensions.javaURI
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path

@TestInstance(PER_CLASS)
private class ConfigurationTests {

    @Test
    fun `reading the configuration when created programmatically`() {

        val databaseURIValue = "postgres://postgres:123456@127.0.0.1:5432/dummy".let(URI::create)
        val environment = Environment.from(
                MyConfigurationAdapter.servicePortKey to "8080",
                "${MyConfigurationAdapter.databaseSectionName}.${MyConfigurationAdapter.database.uriKey}" to databaseURIValue.toString()
        )

        val configuration = MyConfiguration.from(environment)
        val databaseURI = configuration.database.uri

        assertThat(databaseURI).isEqualTo(databaseURIValue)
    }

    @Test
    fun `attempting to read the configuration when a value is invalid`() {

        val servicePortInvalidValue = "hello"
        val environment = Environment.from(
                MyConfigurationAdapter.servicePortKey to servicePortInvalidValue,
                MyConfigurationAdapter.database.uriKey to "postgres://postgres:123456@127.0.0.1:5432/dummy"
        )

        val readingAttempt = runCatching { MyConfiguration.from(environment) }

        assertThat(readingAttempt).isFailure().isInstanceOf<LensFailure>().hasMessage("env 'SERVICE_PORT' must be string")
    }

    @Test
    fun `attempting to read the configuration when a required value is missing`() {

        val environment = Environment.from(
                MyConfigurationAdapter.database.uriKey to "postgres://postgres:123456@127.0.0.1:5432/dummy"
        )

        val readingAttempt = runCatching { MyConfiguration.from(environment) }

        assertThat(readingAttempt).isFailure().isInstanceOf<LensFailure>().hasMessage("env 'SERVICE_PORT' is required")
    }

    @Test
    fun `reading the configuration in cascade`(@TempDir configurationDirectory: Path) {

        val configurationFilePath = configurationDirectory.resolve("application.yml")

        val servicePortValue = 8080
        val databaseURIValue = "postgres://postgres:123456@127.0.0.1:5432/dummy".let(URI::create)
        Files.write(configurationFilePath, listOf("""
            database:
              uri: $databaseURIValue
        """.trimIndent()))

        val defaultConfiguration = Environment.from(MyConfigurationAdapter.servicePortKey to "$servicePortValue")
        val environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYaml(configurationFilePath.toFile()) overrides defaultConfiguration

        val configuration = MyConfiguration.from(environment)
        val servicePort = configuration.servicePort
        val databaseURI = configuration.database.uri

        assertThat(servicePort).isEqualTo(servicePortValue)
        assertThat(databaseURI).isEqualTo(databaseURIValue)
    }
}

private interface MyConfiguration {

    val servicePort: Int
    val database: DatabaseConfiguration

    companion object
}

private interface DatabaseConfiguration {

    val uri: URI

    companion object
}

private class MyConfigurationAdapter(private val environment: Environment, private val rootPath: String? = null) : ConfigurationSection(rootPath), MyConfiguration {

    private val servicePortProperty = EnvironmentKey.port().required(fullPath(servicePortKey))

    override val servicePort: Int = servicePortProperty(environment).value
    override val database: DatabaseConfiguration = DatabaseConfiguration.from(environment, fullPath(databaseSectionName))

    companion object {
        val servicePortKey = EnvironmentKey.k8s.SERVICE_PORT.meta.name
        val database get() = DatabaseConfigurationAdapter.Companion
        const val databaseSectionName = "database"
    }
}

private fun MyConfiguration.Companion.from(environment: Environment, rootPath: String? = null): MyConfiguration = MyConfigurationAdapter(environment, rootPath)
private fun DatabaseConfiguration.Companion.from(environment: Environment, rootPath: String? = null): DatabaseConfiguration = DatabaseConfigurationAdapter(environment, rootPath)

private class DatabaseConfigurationAdapter(private val environment: Environment, private val rootPath: String? = null) : ConfigurationSection(rootPath), DatabaseConfiguration {

    private val uriProperty = EnvironmentKey.javaURI().required(fullPath(uriKey))

    override val uri: URI = uriProperty(environment)

    companion object {
        const val uriKey = "uri"
    }
}