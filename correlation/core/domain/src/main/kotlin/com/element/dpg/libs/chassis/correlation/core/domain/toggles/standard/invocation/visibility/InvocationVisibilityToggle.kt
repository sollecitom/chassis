package com.element.dpg.libs.chassis.correlation.core.domain.toggles.standard.invocation.visibility

import com.element.dpg.libs.chassis.core.domain.identity.StringId
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.Toggle
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.Toggles
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.standard.template.EnumToggle

private object InvocationVisibilityToggle : Toggle<InvocationVisibility, String> by EnumToggle(id = "invocation-visibility".let(::StringId), deserializeValue = InvocationVisibility::valueOf)

val Toggles.Companion.InvocationVisibility: Toggle<InvocationVisibility, String> get() = InvocationVisibilityToggle