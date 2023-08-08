dependencies {
    api(projects.chassisCoreDomain)

    implementation(projects.chassisLoggerCore)
    implementation(projects.chassisConfigurationUtils)

    testImplementation(projects.chassisTestUtils)
}