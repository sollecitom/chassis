package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserAlreadyRegisteredException

class DispatchingApplication(private val userWithEmailAddress: suspend (EmailAddress) -> User) : Application {

    @Suppress("UNCHECKED_CAST")
    override suspend fun <RESULT> invoke(command: ApplicationCommand<RESULT>) = when (command) {
        is RegisterUserCommand.V1 -> process(command) as RESULT
    }

    private suspend fun process(command: RegisterUserCommand.V1): RegisterUserCommand.V1.Result {

        val user = userWithEmailAddress(command.emailAddress)
        return try {
            user.submitRegistrationRequest()
            RegisterUserCommand.V1.Result.Accepted
        } catch (error: UserAlreadyRegisteredException) {
            RegisterUserCommand.V1.Result.Rejected.EmailAddressAlreadyInUse(user.id)
        }
    }
}