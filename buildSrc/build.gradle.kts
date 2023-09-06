import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
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

tasks.withType<DependencyUpdatesTask> {

    checkConstraints = false
    checkBuildEnvironmentConstraints = false
    checkForGradleUpdate = true
    outputFormatter = "json,html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"

    rejectVersionIf {
        (currentVersion.toVersionNumber() > candidate.version.toVersionNumber()) || (currentVersion.toVersionNumber().isStable && !candidate.version.toVersionNumber().isStable)
    }
}

versionCatalogUpdate {
    sortByKey.set(false)
}