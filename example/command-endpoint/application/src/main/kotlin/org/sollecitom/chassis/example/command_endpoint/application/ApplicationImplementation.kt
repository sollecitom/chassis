package org.sollecitom.chassis.example.command_endpoint.application

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.ddd.application.LoggingApplicationAdapter

internal class ApplicationImplementation : Application {

    context(InvocationContext<ACCESS>)
    override suspend fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>): RESULT {

        // What about pre-publishing checks? How to implement this generically?
        // Subscribe to the result, coming from downstream
        //  - how do you know whether you need it? Do you want for the external user to choose whether to get a synchronous response?
        // Publish the message
        // Await the result, coming from downstream
        // Return the result
        TODO("implement")
    }
}

operator fun Application.Companion.invoke(): Application = ApplicationImplementation().let(::LoggingApplicationAdapter)