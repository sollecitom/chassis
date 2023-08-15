dependencies {
    implementation(projects.chassisExampleServiceWriteEndpointAdaptersDrivingWeb)
    implementation(projects.chassisExampleServiceWriteEndpointAdaptersDrivenPulsar)
    implementation(projects.chassisExampleServiceWriteEndpointConfiguration)
    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisWebServiceDomain)

    testImplementation(projects.chassisWebApiTestUtils)
    testImplementation(projects.chassisTestUtils)
    testImplementation(platform(libs.http4k.bom))
    testImplementation(libs.http4k.client.apache.async)
    testImplementation(libs.http4k.client.apache) // TODO remove this after making the async one work with coroutines

    // TODO remove
    implementation(libs.http4k.server.jetty)
}