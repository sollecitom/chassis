dependencies {
    api(projects.chassisExampleServiceWriteEndpointApplication)
    api(projects.chassisWebServiceDomain)
    api(projects.chassisWebApiTestUtils)
    api(projects.chassisTestUtils)
    implementation(projects.chassisKotlinExtensions)

    api(platform(libs.http4k.bom))
    api(libs.http4k.client.apache.async)
    api(libs.http4k.client.apache) // TODO remove this after making the async one work with coroutines
}