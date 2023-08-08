package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier

sealed class RegisterUserCommand<RESULT> : ApplicationCommand<RESULT> {

    class V1(val command: RegisterUser.V1) : RegisterUserCommand<V1.Result>(), RegisterUser by command {

        sealed interface Result {

            data object Accepted : Result

            sealed interface Rejected : Result {

                data class EmailAddressAlreadyInUse(val userId: SortableTimestampedUniqueIdentifier<*>) : Rejected
            }
        }
    }
}

fun RegisterUser.V1.asApplicationCommand() = RegisterUserCommand.V1(this)