//package org.sollecitom.chassis.example.command_endpoint.application.user.registration
//
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//import org.sollecitom.chassis.core.domain.email.EmailAddress
//import org.sollecitom.chassis.core.test.utils.testProvider
//import org.sollecitom.chassis.core.utils.CoreDataGenerator
//import org.sollecitom.chassis.correlation.core.domain.access.Access
//import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
//import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
//import org.sollecitom.chassis.ddd.application.dispatching.ApplicationCommandHandler
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//private class RegisterUserTests : CoreDataGenerator by CoreDataGenerator.testProvider {
//
//    @Test
//    fun `registering a user with an email address never used before`() = runTest {
//
//        val emailAddress = "fresh-address@gmail.com".let(::EmailAddress)
//        val command = RegisterUser.V1(emailAddress = emailAddress)
//        val invocationContext = InvocationContext.unauthenticated()
//        val handler = newHandler()
//
//        val result = with(invocationContext) { handler.process(command) }
//    }
//
//    private fun newHandler(): ApplicationCommandHandler<RegisterUser.V1, RegisterUser.V1.Result, Access> {
//        TODO("Not yet implemented")
//    }
//}