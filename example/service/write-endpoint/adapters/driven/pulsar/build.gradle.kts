dependencies {
//    api(projects.chassisExampleServiceWriteEndpointDomain)
    api(projects.chassisDddDomain)

    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}