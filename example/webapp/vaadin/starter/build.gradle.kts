import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(libs.spring.boot.starter)

    implementation(projects.chassisExampleWebappVaadinConfiguration)
    implementation(projects.chassisKotlinExtensions)
    runtimeOnly(projects.chassisExampleWebappVaadinAdaptersDrivingWeb)
    runtimeOnly(projects.chassisExampleWebappVaadinAdaptersDrivenSdkMemory)
    runtimeOnly(projects.chassisExampleWebappVaadinAdaptersDrivenSdkHttp)

    testImplementation(projects.chassisTestUtils)
}