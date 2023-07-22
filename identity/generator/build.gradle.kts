dependencies {
    api(projects.chassisIdentityDomain)
//    implementation(libs.ulid.creator) # TODO re-enable after https://github.com/f4b6a3/ulid-creator/pull/27 will be merged
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}