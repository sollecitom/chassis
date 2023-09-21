dependencies {
    api(libs.pulsar.client.admin) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
    api(projects.chassisCoreDomain)
    api(projects.chassisConfigurationUtils)

    testImplementation(projects.chassisCoreTestUtils)
}