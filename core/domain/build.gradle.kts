dependencies {
    api(projects.chassisKotlinExtensions)
    implementation(libs.ulid.creator)
    implementation(libs.ksuid.creator)
    implementation(libs.guava)

    testImplementation(projects.chassisTestUtils)
}