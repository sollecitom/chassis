import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.vaadin) // TODO try to move this to driven-adapters-web instead
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(libs.vaadin.spring.boot.starer) // TODO replace with normal Spring boot here

    implementation(projects.chassisExampleWebappVaadinConfiguration)
    implementation(projects.chassisKotlinExtensions)
    runtimeOnly(projects.chassisExampleWebappVaadinAdaptersDrivingWeb)
    runtimeOnly(projects.chassisExampleWebappVaadinAdaptersDrivenSdkMemory)
    runtimeOnly(projects.chassisExampleWebappVaadinAdaptersDrivenSdkHttp)

    testImplementation(projects.chassisTestUtils)
}