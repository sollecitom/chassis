import com.google.cloud.tools.jib.api.buildplan.ImageFormat
import com.google.cloud.tools.jib.gradle.JibExtension
import com.google.cloud.tools.jib.gradle.PlatformParameters
import com.google.cloud.tools.jib.gradle.PlatformParametersSpec
import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import org.gradle.nativeplatform.platform.internal.ArchitectureInternal
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.jib)
    alias(libs.plugins.com.palantir.git.version)
}

dependencies {
    implementation(projects.chassisExampleWriteEndpointAdaptersDrivingWeb)
    implementation(projects.chassisExampleWriteEndpointAdaptersDrivenEvents)
    implementation(projects.chassisExampleWriteEndpointConfiguration)
    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisWebServiceDomain)
    implementation(projects.chassisDddEventFrameworkMemory) // TODO swap this with the pulsar-based one
}

val versionDetails: Closure<VersionDetails> by extra
val gitVersionDetails: VersionDetails = versionDetails.call()
val buildTimestamp: String = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
val dockerBaseImage: String by project
val serviceName = "example-write-endpoint"
val repository = "ghcr.io/sollecitom-chassis/"
val serviceImage = "$repository$serviceName"

val tmpVolume = "/tmp"
val maxRamPercentage = "70.000000"
val customJvmFlags = listOf("-XX:+UnlockExperimentalVMOptions", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=$tmpVolume/java_pid<pid>.hprof", "-XX:MaxRAMPercentage=$maxRamPercentage", "-XX:+UseG1GC", "-XX:+AlwaysPreTouch", "-XX:+UseNUMA", "--enable-preview")
val customArgs = listOf("-Djava.security.egd=file:/dev/./urandom")
val mainAppPort = "8081"
val healthAppPort = "8082"
val containerEnvironment = mutableMapOf("SERVICE_PORT" to mainAppPort, "HEALTH_PORT" to healthAppPort, "BUILD_REVISION" to gitVersionDetails.gitHashFull, "BUILD_TIMESTAMP" to buildTimestamp)
val imageTags = setOf(gitVersionDetails.gitHashFull, "snapshot")
val currentOperatingSystem: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()
val currentArchitecture: ArchitectureInternal = DefaultNativePlatform.getCurrentArchitecture()

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
                "build.git.hash.full" to gitVersionDetails.gitHashFull, "build.timestamp" to buildTimestamp
            )
        )
        filesModificationTime.set(buildTimestamp) // TODO this screws up reproducibility - think whether reproducibility is worth achieving, even at the price of some nonsensical consequences
        creationTime.set(buildTimestamp) // TODO this screws up reproducibility - think whether reproducibility is worth achieving, even at the price of some nonsensical consequences
        mainClass = "org.sollecitom.chassis.example.write_endpoint.starter.StarterKt"
    }
    from {
        image = dockerBaseImage
        platforms {
            configureForOperatingSystem(currentOperatingSystem, currentArchitecture)
        }
    }
    to {
        image = serviceImage
        tags = imageTags
    }
}

// TODO move the lines below into a plugin and import it instead
fun PlatformParametersSpec.configureForOperatingSystem(currentOS: OperatingSystem, currentArchitecture: ArchitectureInternal) {

    when {
        currentOS.isMacOsX && currentArchitecture.isArm64 -> platform { appleSilicon() }
        else -> platform { linux() }
    }
}

fun PlatformParameters.appleSilicon() {
    architecture = "arm64"
    os = "linux"
}

fun PlatformParameters.linux() {
    architecture = "amd64"
    os = "linux"
}