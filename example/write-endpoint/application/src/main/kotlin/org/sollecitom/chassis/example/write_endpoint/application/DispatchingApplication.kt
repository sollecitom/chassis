package org.sollecitom.chassis.example.write_endpoint.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.context.unauthenticatedOrThrow
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.ddd.application.asApplicationEvent
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.write_endpoint.application.user.UserWithPendingRegistration
import org.sollecitom.chassis.example.write_endpoint.domain.user.User
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationEvent
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationRequestWasSubmitted

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
        val event = user.submitRegistrationRequest()
        val applicationEvent = event.asApplicationEvent() // TODO put back the invocation context in the base domain event, and remove this line
        // TODO publish the event
        return event.toOperationResult()
    }

    private fun UserRegistrationEvent.toOperationResult(): RegisterUser.V1.Result = when (this) {
        is UserRegistrationRequestWasSubmitted.V1 -> Accepted(user = UserWithPendingRegistration(id = userId))
        is UserRegistrationRequestWasAlreadySubmitted.V1 -> EmailAddressAlreadyInUse(userId = userId)
    }
}