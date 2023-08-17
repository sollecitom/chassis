val containerBasedSystemTest: SourceSet by sourceSets.creating

val integrationTestTask = tasks.register<Test>("containerBasedSystemTest") {
    description = "Runs container-based system tests."
//    group = "verification"
    useJUnitPlatform()

    testClassesDirs = containerBasedSystemTest.output.classesDirs
    classpath = configurations[containerBasedSystemTest.runtimeClasspathConfigurationName] + containerBasedSystemTest.output

//    shouldRunAfter(tasks.test)
}

fun DependencyHandlerScope.containerBasedSystemTestImplementation(dependency: Any) {

    "containerBasedSystemTestImplementation"(dependency)
}

dependencies {
    containerBasedSystemTestImplementation(projects.chassisExampleServiceWriteEndpointStarter) // TODO maybe remove
    containerBasedSystemTestImplementation(projects.chassisExampleServiceWriteEndpointConfiguration) // TODO remove
    containerBasedSystemTestImplementation(projects.chassisExampleServiceWriteEndpointSystemTestSpecification)
    containerBasedSystemTestImplementation(projects.chassisTestContainersUtils)
}

// TODO enable this
tasks.named("containerBasedSystemTest") {
    dependsOn(":${projects.chassisExampleServiceWriteEndpointStarter.name}:jibDockerBuild")
}