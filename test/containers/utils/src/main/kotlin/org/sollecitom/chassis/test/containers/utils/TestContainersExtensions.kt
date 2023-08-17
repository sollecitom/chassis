package org.sollecitom.chassis.test.containers.utils

import com.github.dockerjava.api.DockerClient
import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.Container

private val docker: DockerClient by lazy { DockerClientFactory.lazyClient() }

fun Container<*>.stopWithoutDeletingContainer() {
    docker.stopContainerCmd(containerId).exec()
}

fun Container<*>.startFromExistingStoppedContainer() {
    docker.startContainerCmd(containerId).exec()
}

fun Container<*>.restart() {
    stopWithoutDeletingContainer()
    startFromExistingStoppedContainer()
}

fun Container<*>.pause() {
    docker.pauseContainerCmd(containerId).exec()
}

fun Container<*>.unpause() {
    docker.unpauseContainerCmd(containerId).exec()
}