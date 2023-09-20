dependencies {
    api(projects.chassisCoreDomain)
    api(projects.chassisJsonUtils)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisJsonTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
}