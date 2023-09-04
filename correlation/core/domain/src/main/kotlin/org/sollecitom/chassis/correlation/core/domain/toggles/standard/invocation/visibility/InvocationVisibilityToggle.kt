package org.sollecitom.chassis.correlation.core.domain.toggles.standard.invocation.visibility

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.correlation.core.domain.toggles.*

private object InvocationVisibilityToggle : Toggle<InvocationVisibility, String> {

    override val id: Id = "invocation-visibility".let(::StringId)

    override operator fun invoke(toggles: Toggles): InvocationVisibility? {

        // TODO refactor
        return toggles[id]?.let(ToggleValue<*>::value)?.let { it as String }?.let { InvocationVisibility.valueOf(it) }
    }

    override fun invoke(value: InvocationVisibility) = EnumToggleValue(id = id, value = value.name)
}

val Toggles.Companion.InvocationVisibility: Toggle<InvocationVisibility, String> get() = InvocationVisibilityToggle