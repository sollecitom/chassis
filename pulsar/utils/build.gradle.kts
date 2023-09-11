dependencies {
    api(libs.pulsarClientAdmin) // TODO exclude com.google.protobuf:protobuf-java:3.15.3

    testImplementation(projects.chassisCoreTestUtils)
}