import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.palantir.gradle.gitversion.GitVersionPlugin
import com.palantir.gradle.gitversion.VersionDetails
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
        jvmArgs = listOf("-XX:+AllowRedefinitionToAddDeleteMethods", "--enable-preview") // TODO read from buildSrc?
    }

    subprojects {
        apply {
            plugin("org.jetbrains.kotlin.jvm")
            plugin<JavaLibraryPlugin>()
            plugin("maven-publish")
        }

        configure<JavaPluginExtension> { Plugins.JavaPlugin.configure(this) }

        publishing {
            repositories {
                mavenLocal()
                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/sollecitom/chassis")
                    credentials {
                        username = project.findProperty("sollecitom.github.user") as String?
                                ?: System.getenv("GITHUB_USERNAME")
                        password = project.findProperty("sollecitom.github.token") as String?
                                ?: System.getenv("GITHUB_TOKEN")
                    }
                }
            }
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

// TODO move to a library and refactor
fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return !isStable
}

// TODO replace with a semver library of choice
fun String.toVersionNumber() = VersionNumber.parse(this)

tasks.withType<DependencyUpdatesTask> {

    checkConstraints = false
    checkBuildEnvironmentConstraints = false
    checkForGradleUpdate = true
    outputFormatter = "json,html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"

    resolutionStrategy {
        componentSelection {
            all {
                when {
                    !candidate.version.isNonStable() && currentVersion.isNonStable() -> reject("Release candidate ${candidate.module}")
                    candidate.version.toVersionNumber() < currentVersion.toVersionNumber() -> reject("Lower version ${candidate.module}")
                }
            }
        }
    }
}

versionCatalogUpdate {
    sortByKey.set(false)
}
