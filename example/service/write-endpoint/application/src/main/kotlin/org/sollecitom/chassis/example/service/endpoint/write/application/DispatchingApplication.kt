package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.context.unauthenticatedOrThrow
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.ddd.application.asApplicationEvent
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.service.endpoint.write.application.user.UserWithPendingRegistration
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationEvent
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted

internal class DispatchingApplication(private val userWithEmailAddress: suspend (EmailAddress) -> User) : Application {

    override suspend fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>, context: InvocationContext<ACCESS>) = when (command) {

        is RegisterUser -> processRegisterUserCommand(command, context.unauthenticatedOrThrow())
        else -> error("Unknown application command $command")
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <RESULT> processRegisterUserCommand(command: RegisterUser<RESULT>, context: InvocationContext<Access.Unauthenticated>): RESULT = when (command) {

        is RegisterUser.V1 -> processRegisterUserCommandV1(command, context) as RESULT
    }

    private suspend fun processRegisterUserCommandV1(command: RegisterUser.V1, context: InvocationContext<Access.Unauthenticated>): RegisterUser.V1.Result {

        val user = userWithEmailAddress(command.emailAddress)
        val event = user.submitRegistrationRequest()
        val applicationEvent = event.asApplicationEvent(context)
        // TODO publish the event
        return event.toOperationResult()
    }

    private fun UserRegistrationEvent.toOperationResult(): RegisterUser.V1.Result = when (this) {
        is UserRegistrationRequestWasSubmitted.V1 -> Accepted(user = UserWithPendingRegistration(id = userId))
        is UserRegistrationRequestWasAlreadySubmitted.V1 -> EmailAddressAlreadyInUse(userId = userId)
    }
}