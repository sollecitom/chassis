package org.sollecitom.chassis.example.write_endpoint.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.LoggingApplicationAdapter
import org.sollecitom.chassis.example.write_endpoint.domain.user.User

operator fun Application.Companion.invoke(userWithEmailAddress: suspend (EmailAddress) -> User): Application = DispatchingApplication(userWithEmailAddress).let(::LoggingApplicationAdapter)