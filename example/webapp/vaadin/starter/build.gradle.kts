dependencies {
    implementation(projects.chassisExampleWebappVaadinConfiguration)
    implementation(projects.chassisKotlinExtensions)
    runtimeOnly(projects.chassisExampleWebappVaadinAdaptersDrivingWeb)
    runtimeOnly(projects.chassisExampleWebappVaadinAdaptersDrivenSdkMemory)
    runtimeOnly(projects.chassisExampleWebappVaadinAdaptersDrivenSdkHttp)

    testImplementation(projects.chassisTestUtils)
}