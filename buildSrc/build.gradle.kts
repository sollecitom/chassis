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
//        classpath(libs.semver4j)
    }
}

dependencies {
    implementation(libs.semver4j)
}