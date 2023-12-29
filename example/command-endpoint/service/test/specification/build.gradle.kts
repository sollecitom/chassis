dependencies {
    api(projects.chassisWebServiceDomain)
    api(projects.chassisWebApiTestUtils)
    api(projects.chassisTestUtils)
    api(projects.chassisCorrelationCoreTestUtils)
    api(projects.chassisPulsarTestUtils)
    api(projects.chassisPulsarJsonSerialization)
    api(projects.chassisExampleEventDomain)
    api(projects.chassisExampleEventSerializationJson)
    api(projects.chassisDddTestUtils)
    api(projects.chassisKotlinExtensions)
    api(platform(libs.http4k.bom))
    api(libs.http4k.client.apache.async)
}