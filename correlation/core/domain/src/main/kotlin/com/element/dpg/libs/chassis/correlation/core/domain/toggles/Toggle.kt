package com.element.dpg.libs.chassis.correlation.core.domain.toggles

import com.element.dpg.libs.chassis.core.domain.traits.Identifiable

interface Toggle<VALUE : Any, out SERIALIZED_VALUE : Any> : Identifiable, ToggleValueSerializer<VALUE, SERIALIZED_VALUE>, ToggleValueExtractor<VALUE>