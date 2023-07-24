dependencies {
    api(libs.slf4j.api)
    api(platform(libs.kotlinx.coroutines.bom))
    api(libs.kotlinx.coroutines.slf4j)

    testImplementation(projects.chassisTestUtils)
}