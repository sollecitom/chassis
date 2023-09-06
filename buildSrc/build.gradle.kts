import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent
import com.vdurmont.semver4j.Semver

plugins {
    `kotlin-dsl`
    alias(libs.plugins.com.github.ben.manes.versions)
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
}

repositories {
    mavenCentral()
}

buildscript {
    dependencies {
        classpath(libs.semver4j)
    }
}

dependencies {
    implementation(libs.semver4j)
}

// TODO turn this whole dependency update thing into a plugin

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

versionCatalogUpdate {
    sortByKey.set(false)
}