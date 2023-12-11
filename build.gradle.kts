import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent
import com.palantir.gradle.gitversion.GitVersionPlugin
import com.palantir.gradle.gitversion.VersionDetails
import com.vdurmont.semver4j.Semver
import groovy.lang.Closure
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript { repositories { RepositoryConfiguration.BuildScript.apply(this) } }

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    idea
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.com.palantir.git.version)
    alias(libs.plugins.com.github.ben.manes.versions)
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
    alias(libs.plugins.jib) apply false
}

apply<GitVersionPlugin>()

val parentProject = this
val currentVersion: String by project
val versionCatalogName: String by project
val versionDetails: Closure<VersionDetails> by extra
val gitVersion = versionDetails()

allprojects {

    group = ProjectSettings.groupId
    version = currentVersion

    repositories { RepositoryConfiguration.Modules.apply(this, project) }

    apply<IdeaPlugin>()
    idea { module { inheritOutputDirs = true } }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.apply {
            jvmTarget = KotlinConfiguration.TargetJvm.version
            javaParameters = KotlinConfiguration.Compiler.generateJavaParameters
            freeCompilerArgs = KotlinConfiguration.Compiler.arguments
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        if (System.getenv("CI") != null) {
            maxParallelForks = 1
            maxHeapSize = "1g"
        } else {
            maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        }
        testLogging {
            showStandardStreams = false
            exceptionFormat = TestExceptionFormat.FULL
        }
        jvmArgs = JvmConfiguration.testArgs
    }

    subprojects {
        apply {
            plugin("org.jetbrains.kotlin.jvm")
            plugin<JavaLibraryPlugin>()
            plugin("maven-publish")
        }

        java(Plugins.JavaPlugin::configure)

        publishing {
            repositories { RepositoryConfiguration.Publications.apply(this, project) }
            publications {
                create<MavenPublication>("${name}-maven") {
                    groupId = this@allprojects.group.toString()
                    artifactId = project.name
                    version = this@allprojects.version.toString()

                    from(components["java"])
                    println("Created publication ${this.groupId}:${this.artifactId}:${this.version}")
                }
            }
        }
    }
}

// TODO turn this whole dependency update thing into a plugin (then remove the lines below)

fun String.toVersionNumber() = Semver(this)

val ComponentSelectionWithCurrent.currentSemanticVersion: Semver get() = Semver(currentVersion)
val ComponentSelectionWithCurrent.candidateSemanticVersion: Semver get() = Semver(candidate.version)

fun ComponentSelectionWithCurrent.wouldDowngradeVersion(): Boolean = currentSemanticVersion > candidateSemanticVersion
fun ComponentSelectionWithCurrent.wouldDestabilizeAStableVersion(): Boolean = currentSemanticVersion.isStable && !candidateSemanticVersion.isStable

tasks.withType<DependencyUpdatesTask> {

    checkConstraints = true
    checkBuildEnvironmentConstraints = false
    checkForGradleUpdate = true
    outputFormatter = "json,html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"

    rejectVersionIf {
        wouldDowngradeVersion() || wouldDestabilizeAStableVersion()
    }
}

versionCatalogUpdate { // TODO add https://github.com/gradle-update/update-gradle-wrapper-action to the CI script when you'll have one
    sortByKey.set(false)
    keep {
        keepUnusedVersions = true
        keepUnusedLibraries = true
        keepUnusedPlugins = true
    }
}

val containerBasedServiceTest: Task = tasks.register("containerBasedServiceTest").get()
