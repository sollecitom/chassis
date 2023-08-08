package org.sollecitom.chassis.core.test.utils

import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.core.utils.provider

val WithCoreGenerators.Companion.testProvider: WithCoreGenerators by lazy { WithCoreGenerators.provider() }