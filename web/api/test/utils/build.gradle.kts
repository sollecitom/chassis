dependencies {
    api(projects.chassisWebServiceDomain)
    api(projects.chassisWebApiUtils)
    api(projects.chassisTestUtils)
    api(projects.chassisDddApplication)
    api(platform(libs.http4k.bom))
    api(libs.http4k.client.apache.async)

    implementation(projects.chassisKotlinExtensions)
}
