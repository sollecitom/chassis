package org.sollecitom.chassis.example.command_endpoint.application.predicate.search

import org.junit.jupiter.api.TestInstance
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator

// TODO add input checks for description, email address, and product code in the HTTP endpoint
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
private class PredicateSearchTests : CoreDataGenerator by CoreDataGenerator.testProvider {

//    @Test
//    fun `registering a user with an email address never used before`() = runTest {
//
//        val emailAddress = "fresh-address@gmail.com".let(::EmailAddress)
//        val command = RegisterUser(emailAddress = emailAddress)
//        val invocationContext = InvocationContext.unauthenticated()
//        val handler = newHandler { _: EmailAddress -> null }
//
//        val result = with(invocationContext) { handler.process(command) }
//
//        assertThat(result).wasAccepted()
//    }
//
//    @Test
//    fun `registering a user with an email address that was used before the command was received`() = runTest {
//
//        val emailAddress = "stale-address@gmail.com".let(::EmailAddress)
//        val command = RegisterUser(emailAddress = emailAddress)
//        val invocationContext = InvocationContext.unauthenticated()
//        val anotherUser = User(id = newId.forEntities())
//        val handler = newHandler { address: EmailAddress -> address.takeIf { it == emailAddress }?.let { anotherUser } }
//
//        val result = with(invocationContext) { handler.process(command) }
//
//        assertThat(result).wasRejectedBecauseAnotherUserHasTheSameEmailAddress(user = anotherUser)
//    }
//
//    private fun newHandler(process: suspend context(InvocationContext<Access>)(CommandWasReceived<RegisterUser>) -> RegisterUser.Result): CommandHandler<RegisterUser, RegisterUser.Result, Access> {
//
//        val processor = InMemoryCommandProcessor(process)
//        return RegisterUserHandler(receivedCommandPublisher = processor, commandResultSubscriber = processor, uniqueIdGenerator = this@RegisterUserTests, timeGenerator = this@RegisterUserTests)
//    }
//
//    private fun newHandler(findExistingUser: suspend (EmailAddress) -> User? = { null }) = newHandler(process = { event ->
//
//        val existingUser = findExistingUser(event.command.emailAddress)
//        existingUser?.let { EmailAddressAlreadyInUse(user = it) } ?: Accepted(user = UserWithPendingRegistration(id = newId.forEntities()))
//    })
//
//    private fun Assert<RegisterUser.Result>.wasAccepted() = given { result -> assertThat(result).isInstanceOf<Accepted>() }
//
//    private fun Assert<RegisterUser.Result>.wasRejectedBecauseAnotherUserHasTheSameEmailAddress(user: User) = given { result -> assertThat(result).isEqualTo(EmailAddressAlreadyInUse(user = user)) }
}