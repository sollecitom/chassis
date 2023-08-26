package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.context.unauthenticatedOrThrow
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserAlreadyRegisteredException

internal class DispatchingApplication(private val userWithEmailAddress: suspend (EmailAddress) -> User) : Application {

    override suspend fun <RESULT, ACCESS : Access<SortableTimestampedUniqueIdentifier<*>>> invoke(command: ApplicationCommand<RESULT, ACCESS>, context: InvocationContext<ACCESS>) = when (command) {

        is RegisterUser -> processRegisterUserCommand(command, context.unauthenticatedOrThrow()) // TODO add a test case
        else -> error("Unknown application command $command")
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <RESULT> processRegisterUserCommand(command: RegisterUser<RESULT>, context: InvocationContext<Access.Unauthenticated>): RESULT = when (command) {

        is RegisterUser.V1 -> processRegisterUserCommandV1(command, context) as RESULT
    }

    private suspend fun processRegisterUserCommandV1(command: RegisterUser.V1, context: InvocationContext<Access.Unauthenticated>): RegisterUser.V1.Result {

        val user = userWithEmailAddress(command.emailAddress)
        val attempt = runCatching { user.submitRegistrationRequest(context) }
        return attempt.map { Accepted(user = user.withPendingRegistration()) }.recoverCatching { if (it is UserAlreadyRegisteredException) EmailAddressAlreadyInUse(user.id) else throw it }.getOrThrow()
    }

    private fun User.withPendingRegistration() = User.WithPendingRegistration(id = id)
}