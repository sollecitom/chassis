package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserAlreadyRegisteredException

class DispatchingApplication(private val userWithEmailAddress: suspend (EmailAddress) -> User) : Application {

    @Suppress("UNCHECKED_CAST")
    override suspend fun <RESULT> invoke(command: ApplicationCommand<RESULT>) = when (command) {

        is RegisterUser -> when (command) {
            is RegisterUser.V1 -> process(command) as RESULT
        }

        else -> error("Unknown application command $command")
    }

    private suspend fun process(command: RegisterUser.V1): RegisterUser.V1.Result {

        val user = userWithEmailAddress(command.emailAddress)
        val attempt = runCatching { user.submitRegistrationRequest() }
        return attempt.map { Accepted }.recoverCatching { if (it is UserAlreadyRegisteredException) EmailAddressAlreadyInUse(user.id) else throw it }.getOrThrow()
    }
}