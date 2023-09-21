dependencies {
    api(projects.chassisCorrelationCoreDomain)

    implementation(projects.chassisKotlinExtensions)
    implementation(libs.yet.another.user.agent.analyzer) // TODO API?
    implementation(projects.chassisLoggerCore)

    runtimeOnly(libs.log4j.api)
    runtimeOnly(libs.log4j.over.slf4j)

    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testRuntimeOnly(projects.chassisLoggerSlf4jAdapter)
}