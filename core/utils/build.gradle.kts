dependencies {
    api(projects.chassisCoreDomain)
    api(projects.chassisConfigurationUtils)

    implementation(projects.chassisLoggerCore)

    testImplementation(projects.chassisTestUtils)
}