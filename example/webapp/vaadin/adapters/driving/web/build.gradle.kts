plugins {
    java
    alias(libs.plugins.vaadin)
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    implementation(libs.vaadin.spring.boot.starer)

    implementation(projects.chassisExampleWebappVaadinConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}