import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent
import com.vdurmont.semver4j.Semver

plugins {
    alias(libs.plugins.kotlin.jvm)
    `kotlin-dsl`
    alias(libs.plugins.com.github.ben.manes.versions)
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
}

repositories {
    mavenCentral()
}

buildscript {
    dependencies {
        classpath(libs.semver4j) // TODO remove
    }
}

dependencies {
    implementation(libs.semver4j) // TODO remove
}