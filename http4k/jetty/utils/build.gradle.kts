dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.server.jetty)
    api(projects.chassisHttp4kUtils)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
    testImplementation(libs.http4k.client.apache)
    testImplementation(projects.chassisLoggerCore)

    testImplementation("org.slf4j:slf4j-nop:2.0.7")

    testRuntimeOnly(projects.chassisLoggerSlf4jAdapter)
}