package com.element.dpg.libs.chassis.correlation.core.domain.toggles.standard.invocation.visibility

import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggle
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.correlation.core.domain.toggles.standard.template.EnumToggle

private object InvocationVisibilityToggle : Toggle<InvocationVisibility, String> by EnumToggle(id = "invocation-visibility".let(::StringId), deserializeValue = InvocationVisibility::valueOf)

val Toggles.Companion.InvocationVisibility: Toggle<InvocationVisibility, String> get() = InvocationVisibilityToggle