import com.google.cloud.tools.jib.api.buildplan.ImageFormat
import com.google.cloud.tools.jib.gradle.JibExtension
import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.jib)
    alias(libs.plugins.com.palantir.git.version)
}

dependencies {
    implementation(projects.chassisExampleServiceWriteEndpointAdaptersDrivingWeb)
    implementation(projects.chassisExampleServiceWriteEndpointAdaptersDrivenPulsar)
    implementation(projects.chassisExampleServiceWriteEndpointConfiguration)
    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisWebServiceDomain)

    testImplementation(projects.chassisWebApiTestUtils)
    testImplementation(projects.chassisTestUtils)
    testImplementation(platform(libs.http4k.bom))
    testImplementation(libs.http4k.client.apache.async)
    testImplementation(libs.http4k.client.apache) // TODO remove this after making the async one work with coroutines

    // TODO remove
    implementation(libs.http4k.server.jetty)
}

val versionDetails: Closure<VersionDetails> by extra
val gitVersionDetails: VersionDetails = versionDetails.call()
val buildTimestamp: String = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
val dockerBaseImage: String by project
val serviceName = "example-write-endpoint"
val repository = "ghcr.io/sollecitom-chassis/"
val serviceImage = "$repository$serviceName" // TODO change this to "$repository$serviceName" after GitHub actions

val tmpVolume = "/tmp"
val maxRamPercentage = "70.000000"
val customJvmFlags = listOf("-XX:+UnlockExperimentalVMOptions", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=$tmpVolume/java_pid<pid>.hprof", "-XX:MaxRAMPercentage=$maxRamPercentage", "-XX:+UseG1GC", "-XX:+AlwaysPreTouch", "-XX:+UseNUMA")
val customArgs = listOf("-Djava.security.egd=file:/dev/./urandom")
val mainAppPort = "8085"
val healthAppPort = "8085"
val containerEnvironment = mutableMapOf("SERVICE_PORT" to mainAppPort, "HEALTH_PORT" to healthAppPort, "BUILD_REVISION" to gitVersionDetails.gitHashFull, "BUILD_TIMESTAMP" to buildTimestamp)
val imageTags = setOf(gitVersionDetails.gitHashFull, "snapshot")

configure<JibExtension> {
    container {
        args = customArgs
        jvmFlags = customJvmFlags
        volumes = listOf(tmpVolume)
        environment = containerEnvironment
        user = "nobody"
        format = ImageFormat.OCI
        labels.set(
            mapOf(
                "build.git.hash.full" to gitVersionDetails.gitHashFull,
                "build.timestamp" to buildTimestamp
            )
        )
        ports = listOf(mainAppPort, healthAppPort)
        filesModificationTime.set(buildTimestamp)
        creationTime.set(buildTimestamp) // TODO this is to achieve build reproducibility - think whether reproducibility is worth it
        mainClass = "org.sollecitom.chassis.example.service.endpoint.write.starter.StarterKt"
    }
    from {
        image = dockerBaseImage
    }
    to {
        image = serviceImage
        tags = imageTags
    }
}