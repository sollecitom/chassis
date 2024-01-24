package org.sollecitom.chassis.configuration.utils

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.http4k.lens.long
import org.sollecitom.chassis.lens.core.extensions.naming.name
import kotlin.random.Random

private val randomSeedKey = EnvironmentKey.long().defaulted("random.seed", Random.nextLong(), "The seed (long) used to initialise random data generation")
val EnvironmentKey.randomSeed get() = randomSeedKey

private val instanceIdKey = EnvironmentKey.int().required("instance.id", "The service node ID in the cluster. Minimum is 0. Used to generate TSIDs")
val EnvironmentKey.instanceId get() = instanceIdKey

private val instancesGroupMaxSizeKey = EnvironmentKey.int().required("instance.group.max.size", "The maximum instances count. Minimum is 256. Used to generate TSIDs.")
val EnvironmentKey.instanceGroupMaxSize get() = instancesGroupMaxSizeKey

private val instanceGroupNameKey = EnvironmentKey.name().required("instance.group.name", "The name of the instance group. Basically, the service name.")
val EnvironmentKey.instanceGroupName get() = instanceGroupNameKey