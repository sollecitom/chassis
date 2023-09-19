package org.sollecitom.chassis.example.write_endpoint.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.context.unauthenticatedOrThrow
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.example.event.domain.Published
import org.sollecitom.chassis.example.event.domain.UserRegistrationEvent
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.write_endpoint.application.user.UserWithPendingRegistration
import org.sollecitom.chassis.example.write_endpoint.domain.user.User

internal class DispatchingApplication(private val userWithEmailAddress: suspend (EmailAddress) -> User) : Application {

    context(InvocationContext<ACCESS>)
    override suspend fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>) = when (command) {
        is RegisterUser -> with(unauthenticatedOrThrow()) { processRegisterUserCommand(command) }
        else -> error("Unknown application command $command")
    }

    context(InvocationContext<Access.Unauthenticated>)
    @Suppress("UNCHECKED_CAST")
    private suspend fun <RESULT> processRegisterUserCommand(command: RegisterUser<RESULT>): RESULT = when (command) {

        is RegisterUser.V1 -> processRegisterUserCommandV1(command) as RESULT
    }

    context(InvocationContext<Access.Unauthenticated>)
    private suspend fun processRegisterUserCommandV1(command: RegisterUser.V1): RegisterUser.V1.Result {

        val user = userWithEmailAddress(command.emailAddress)
        return user.submitRegistrationRequest().toOperationResult()
    }

    private fun Published<UserRegistrationEvent>.toOperationResult(): RegisterUser.V1.Result = when (event) {
        is UserRegistrationRequestWasSubmitted.V1 -> Accepted(user = UserWithPendingRegistration(id = event.userId))
        is UserRegistrationRequestWasAlreadySubmitted.V1 -> EmailAddressAlreadyInUse(userId = event.userId)
    }
}