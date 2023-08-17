import org.unbrokendome.gradle.plugins.testsets.dsl.testSets

plugins {
    alias(libs.plugins.test.sets)
}

testSets {
    "containerBasedSystemTest"()
}

dependencies {
    containerTestImplementation(projects.chassisExampleServiceWriteEndpointStarter) // TODO maybe remove
    containerTestImplementation(projects.chassisExampleServiceWriteEndpointConfiguration) // TODO remove
    containerTestImplementation(projects.chassisExampleServiceWriteEndpointSystemTestSpecification)
    containerTestImplementation(projects.chassisTestContainersUtils)
}

fun DependencyHandlerScope.containerTestImplementation(dependency: Any) {

    "containerBasedSystemTestImplementation"(dependency)
}

tasks.named("containerBasedSystemTest") {
    dependsOn(":${projects.chassisExampleServiceWriteEndpointStarter.name}:jibDockerBuild")
}