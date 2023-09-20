dependencies {
    api(libs.pulsarClientAdmin) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
    api(projects.chassisCoreDomain)
    api(projects.chassisConfigurationUtils)

    testImplementation(projects.chassisCoreTestUtils)
}