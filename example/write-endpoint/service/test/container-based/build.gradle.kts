val containerBasedServiceTest: SourceSet by sourceSets.creating

val integrationTestTask = tasks.register<Test>("containerBasedServiceTest") {
    description = "Runs container-based service tests."
    group = "verification"
    useJUnitPlatform()

    testClassesDirs = containerBasedServiceTest.output.classesDirs
    classpath = configurations[containerBasedServiceTest.runtimeClasspathConfigurationName] + containerBasedServiceTest.output

    shouldRunAfter(tasks.test)
}

fun DependencyHandlerScope.containerBasedServiceTestImplementation(dependency: Any) {

    "containerBasedServiceTestImplementation"(dependency)
}

dependencies {
    containerBasedServiceTestImplementation(projects.chassisExampleWriteEndpointConfiguration)
    containerBasedServiceTestImplementation(projects.chassisExampleWriteEndpointServiceTestSpecification)
    containerBasedServiceTestImplementation(projects.chassisTestContainersUtils)
    containerBasedServiceTestImplementation(projects.chassisCoreTestUtils)
}

val containerBasedSystemTestTask: TaskProvider<Task> = tasks.named("containerBasedServiceTest") {
    dependsOn(":${projects.chassisExampleWriteEndpointServiceStarter.name}:jibDockerBuild")
}

tasks.named("check") {
    dependsOn(containerBasedSystemTestTask)
}