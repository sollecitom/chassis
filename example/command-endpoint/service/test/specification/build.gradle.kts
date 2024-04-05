dependencies {
    api(projects.chassisWebServiceDomain)
    api(projects.chassisWebApiTestUtils)
    api(projects.chassisTestUtils)
    api(projects.chassisCorrelationCoreTestUtils)
    api(projects.chassisExampleSharedModelDomain)
    api(projects.chassisExampleSharedModelSerializationJson) // TODO replace with Avro
    api(projects.chassisDddTestUtils)
    api(projects.chassisKotlinExtensions)
    api(projects.chassisPulsarMessagingTestUtils)
    api(platform(libs.http4k.bom))
    api(libs.http4k.client.apache.async)
    implementation(projects.chassisPulsarJsonSerialization)
}