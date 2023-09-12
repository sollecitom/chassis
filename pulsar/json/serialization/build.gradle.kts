dependencies {
    api(projects.chassisJsonUtils)
    api(projects.chassisPulsarUtils)

    implementation(projects.chassisLoggerCore)


    testImplementation(projects.chassisCoreTestUtils)
}