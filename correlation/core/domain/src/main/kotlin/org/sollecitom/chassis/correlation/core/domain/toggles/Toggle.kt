package org.sollecitom.chassis.correlation.core.domain.toggles

import org.sollecitom.chassis.core.domain.traits.Identifiable

interface Toggle<VALUE : Any, out SERIALIZED_VALUE : Any> : Identifiable, ToggleValueSerializer<VALUE, SERIALIZED_VALUE>, ToggleValueExtractor<VALUE>