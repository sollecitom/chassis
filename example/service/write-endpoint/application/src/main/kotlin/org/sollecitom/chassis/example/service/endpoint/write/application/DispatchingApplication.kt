package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserAlreadyRegisteredException

internal class DispatchingApplication(private val userWithEmailAddress: suspend (EmailAddress) -> User) : Application {

    override suspend fun <RESULT> invoke(command: ApplicationCommand<RESULT>) = when (command) {

        is RegisterUser -> processRegisterUserCommand(command)
        else -> error("Unknown application command $command")
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <RESULT> processRegisterUserCommand(command: RegisterUser<RESULT>): RESULT = when (command) {

        is RegisterUser.V1 -> processRegisterUserCommandV1(command) as RESULT
    }

    private suspend fun processRegisterUserCommandV1(command: RegisterUser.V1): RegisterUser.V1.Result {

        val user = userWithEmailAddress(command.emailAddress)
        val attempt = runCatching { user.submitRegistrationRequest() }
        return attempt.map { Accepted(user = user.withPendingRegistration()) }.recoverCatching { if (it is UserAlreadyRegisteredException) EmailAddressAlreadyInUse(user.id) else throw it }.getOrThrow()
    }

    private fun User.withPendingRegistration() = User.WithPendingRegistration(id = id)
}