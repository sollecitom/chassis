dependencies {
    api(projects.chassisWebServiceDomain)
    api(projects.chassisWebApiUtils)
    api(projects.chassisTestUtils)
    api(platform(libs.http4k.bom))
    api(libs.http4k.client.apache.async)

    implementation(projects.chassisKotlinExtensions)
}
