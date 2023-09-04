package org.sollecitom.chassis.correlation.core.domain.toggles.standard.invocation.visibility

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.correlation.core.domain.toggles.EnumToggleValue
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggle
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.correlation.core.domain.toggles.withToggle

private object InvocationVisibilityToggle : Toggle<InvocationVisibility, String> {

    override val id: Id = "invocation-visibility".let(::StringId)

    override fun invoke(value: InvocationVisibility) = EnumToggleValue(id = id, value = value.name)
}

val Toggles.Companion.InvocationVisibility: Toggle<InvocationVisibility, String> get() = InvocationVisibilityToggle

fun Toggles.withCustomInvocationVisibility(visibility: InvocationVisibility): Toggles = withToggle(Toggles.InvocationVisibility, visibility)