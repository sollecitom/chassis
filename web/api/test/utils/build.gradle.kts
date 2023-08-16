dependencies {
    api(projects.chassisWebServiceDomain)
    api(projects.chassisWebApiUtils)
    api(projects.chassisTestUtils)
    api(platform(libs.http4k.bom))
    api(libs.http4k.client.apache.async)
    api(libs.http4k.client.apache) // TODO remove this after making the async one work with coroutines

    implementation(projects.chassisKotlinExtensions)
}
