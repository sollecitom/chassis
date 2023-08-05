package org.sollecitom.chassis.example.service.endpoint.write.application

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.traits.Identifiable
import org.sollecitom.chassis.core.domain.traits.Timestamped
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.core.domain.versioning.Versioned

@TestInstance(PER_CLASS)
private class CommandProcessingTests {

    @Test
    fun `registering users`() = runTest {

        val application = newApplication()
        val emailAddress = EmailAddress("someone@somedomain.com")
        val registerUser = registerUserCommand(emailAddress = emailAddress)
    }

    private fun registerUserCommand(emailAddress: EmailAddress, id: SortableTimestampedUniqueIdentifier<*> = newULID(), timestamp: Instant = clock.now()): RegisterUser.V1 {

        return RegisterUser.V1(emailAddress = emailAddress, id = id, timestamp = timestamp)
    }

    private fun newApplication(): Application {
        TODO("Not yet implemented")
    }
}

interface RegisterUser : Command {

    val emailAddress: EmailAddress

    class V1(override val emailAddress: EmailAddress, override val id: SortableTimestampedUniqueIdentifier<*>, override val timestamp: Instant) : RegisterUser {

        override val type: Command.Type get() = Type
        override val version get() = Companion.version

        object Type : Command.Type {
            override val id = Name("COMMAND-REGISTER_USER-V${version.value}")
        }

        companion object {
            val version = IntVersion(1)
        }
    }
}

interface Command : Instruction { // TODO add context

    override val type: Type

    interface Type : Happening.Type
}

interface Query : Instruction {

    override val type: Type

    interface Type : Happening.Type
}

interface Event : Happening {

    override val type: Type

    interface Type : Happening.Type
}

interface Instruction : Happening

interface Happening : Identifiable<SortableTimestampedUniqueIdentifier<*>>, Timestamped, Versioned<IntVersion> {

    val type: Type

    interface Type : Identifiable<Name>
}

interface Application {

}

// TODO move
private val newULID = UniqueIdFactory().ulid
private val clock: Clock = Clock.System