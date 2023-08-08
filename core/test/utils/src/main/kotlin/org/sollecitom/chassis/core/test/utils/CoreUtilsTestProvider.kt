package org.sollecitom.chassis.core.test.utils

import org.sollecitom.chassis.core.utils.WithCoreUtils
import org.sollecitom.chassis.core.utils.provider

val WithCoreUtils.Companion.testProvider: WithCoreUtils by lazy { WithCoreUtils.provider() }