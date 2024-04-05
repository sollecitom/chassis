package org.sollecitom.chassis.example.command_endpoint.domain.predicate.search

import org.sollecitom.chassis.core.domain.email.EmailAddress

interface EmailAddressValidator {

    fun validate(emailAddress: EmailAddress): Result

    sealed interface Result {

        data object Success : Result

        sealed class Failure(val explanation: String) : Result
    }
}