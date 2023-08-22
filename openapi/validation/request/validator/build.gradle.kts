dependencies {
    api(projects.chassisOpenapiParser) // TODO should this be hidden as implementation instead?
    api(libs.swagger.request.validator) // TODO should this be hidden as implementation instead?

    implementation(projects.chassisKotlinExtensions)
}