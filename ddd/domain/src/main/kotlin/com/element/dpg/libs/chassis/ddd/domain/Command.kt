package com.element.dpg.libs.chassis.ddd.domain

import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

interface Command<out RESULT, out ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> : Instruction { // TODO remove access from here?

    val accessRequirements: AccessRequirements

    sealed class AccessRequirements {

        abstract fun check(context: InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>): Result

        data object None : AccessRequirements() {
            override fun check(context: InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>) = Result.Valid
        }

        data object UnauthenticatedAccessOnly : AccessRequirements() {

            override fun check(context: InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>) = when {
                context.access.isAuthenticated -> Result.Invalid.Authenticated
                else -> Result.Valid
            }
        }

        data object AuthenticatedAccessOnly : AccessRequirements() {

            override fun check(context: InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>) = when {
                !context.access.isAuthenticated -> Result.Invalid.Unauthenticated
                else -> Result.Valid
            }
        }

        sealed class Result {

            data object Valid : Result()

            sealed class Invalid(val message: String) : Result() {

                data object Unauthenticated : Invalid("Ony available to authenticated access")
                data object Authenticated : Invalid("Ony available to unauthenticated access")
            }
        }
    }

    companion object
}

inline fun Command.AccessRequirements.Result.ifInvalid(action: (Command.AccessRequirements.Result.Invalid) -> Unit) {
    if (this is Command.AccessRequirements.Result.Invalid) {
        action(this)
    }
}