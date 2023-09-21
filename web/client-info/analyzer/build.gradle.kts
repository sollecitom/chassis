dependencies {
    api(projects.chassisWebClientInfoDomain)

    implementation(projects.chassisKotlinExtensions)
    implementation(libs.yet.another.user.agent.analyzer)
    implementation(projects.chassisLoggerCore)

    runtimeOnly(libs.log4j.to.slf4j)

    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testRuntimeOnly(projects.chassisLoggerSlf4jAdapter)
}