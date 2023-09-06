plugins { `kotlin-dsl` }

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.vdurmont:semver4j:3.1.0") // TODO move this to the dependencies plugin
}
