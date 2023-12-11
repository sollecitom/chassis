package org.sollecitom.chassis.configuration.utils

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.http4k.lens.long
import kotlin.random.Random

private val randomSeedKey = EnvironmentKey.long().defaulted("random.seed", Random.nextLong(), "The seed (long) used to initialise random data generation")
val EnvironmentKey.randomSeed get() = randomSeedKey

private val nodeIdKey = EnvironmentKey.int().required("cluster.node.id", "The service node ID in the cluster. Minimum is 0. Used to generate TSIDs")
val EnvironmentKey.nodeId get() = nodeIdKey

private val maximumNodesCountKey = EnvironmentKey.int().required("cluster.nodes.count", "The maximum nodes count. Minimum is 256. Used to generate TSIDs.")
val EnvironmentKey.maximumNodesCount get() = maximumNodesCountKey