val containerBasedSystemTest: SourceSet by sourceSets.creating

val integrationTestTask = tasks.register<Test>("containerBasedSystemTest") {
    description = "Runs container-based system tests."
    group = "verification"
    useJUnitPlatform()

    testClassesDirs = containerBasedSystemTest.output.classesDirs
    classpath = configurations[containerBasedSystemTest.runtimeClasspathConfigurationName] + containerBasedSystemTest.output

    shouldRunAfter(tasks.test)
}

fun DependencyHandlerScope.containerBasedSystemTestImplementation(dependency: Any) {

    "containerBasedSystemTestImplementation"(dependency)
}

dependencies {
    containerBasedSystemTestImplementation(projects.chassisExampleWriteEndpointConfiguration)
    containerBasedSystemTestImplementation(projects.chassisExampleWriteEndpointServiceTestSpecification)
    containerBasedSystemTestImplementation(projects.chassisTestContainersUtils)
    containerBasedSystemTestImplementation(projects.chassisCoreTestUtils)
}

val containerBasedSystemTestTask: TaskProvider<Task> = tasks.named("containerBasedSystemTest") {
    dependsOn(":${projects.chassisExampleWriteEndpointServiceStarter.name}:jibDockerBuild")
}

tasks.named("check") {
    dependsOn(containerBasedSystemTestTask)
}