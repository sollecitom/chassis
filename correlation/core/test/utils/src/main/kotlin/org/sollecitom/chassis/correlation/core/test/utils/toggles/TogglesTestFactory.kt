package org.sollecitom.chassis.correlation.core.test.utils.toggles

import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.toggles.ToggleValue
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles

context(WithCoreGenerators)
fun Toggles.Companion.create(values: Set<ToggleValue<*>> = emptySet()) = Toggles(values)