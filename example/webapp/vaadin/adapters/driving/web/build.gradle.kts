import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    alias(libs.plugins.spring.boot) // TODO remove?
    alias(libs.plugins.vaadin) // TODO try to move this to driven-adapters-web instead
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(libs.vaadin.spring.boot.starer)

    implementation(projects.chassisExampleWebappVaadinConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}