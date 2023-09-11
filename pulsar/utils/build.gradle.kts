dependencies {
    api(libs.pulsarClientAdmin) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }

    testImplementation(projects.chassisCoreTestUtils)
}