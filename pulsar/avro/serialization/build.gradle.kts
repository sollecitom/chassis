dependencies {
    api(projects.chassisAvroSerializationUtils)
    api(projects.chassisPulsarUtils)

    implementation(projects.chassisLoggerCore)


    testImplementation(projects.chassisCoreTestUtils)
}