import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    alias(libs.plugins.vaadin)
    alias(libs.plugins.spring.boot) // TODO remove?
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(libs.vaadin.spring.boot.starer)

    implementation(projects.chassisExampleWebappVaadinConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}