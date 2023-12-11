package org.sollecitom.chassis.core.test.utils

import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.provider

val CoreDataGenerator.Companion.testProvider: CoreDataGenerator by lazy { CoreDataGenerator.provider(nodeId = 0, maximumNodesCount = 256) }