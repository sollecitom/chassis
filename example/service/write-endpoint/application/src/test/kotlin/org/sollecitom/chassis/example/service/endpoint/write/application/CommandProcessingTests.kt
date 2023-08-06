package org.sollecitom.chassis.example.service.endpoint.write.application

import assertk.assertThat
import assertk.assertions.isInstanceOf
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
import org.sollecitom.chassis.example.service.endpoint.write.application.RegisterUserCommandV1.Result.Accepted

@TestInstance(PER_CLASS)
private class CommandProcessingTests {

    @Test
    fun `registering a new user`() = runTest {

        val application = newApplication()
        val emailAddress = EmailAddress("someone@somedomain.com")
        val registerUser = registerUser(emailAddress = emailAddress).asApplicationCommand()

        val result = application(registerUser)

        assertThat(result).isInstanceOf<Accepted>()
    }

    private fun registerUser(emailAddress: EmailAddress, id: SortableTimestampedUniqueIdentifier<*> = newULID(), timestamp: Instant = clock.now()): RegisterUser.V1 {

        return RegisterUser.V1(emailAddress = emailAddress, id = id, timestamp = timestamp)
    }

    private fun newApplication(): Application = DispatchingApplication()
}

class DispatchingApplication : Application {

    override suspend fun <RESULT> invoke(command: ApplicationCommand<RESULT>): RESULT {
        return Accepted as RESULT
    }
}

interface Application {

    suspend operator fun <RESULT> invoke(command: ApplicationCommand<RESULT>): RESULT
}

sealed interface ApplicationCommand<out RESULT> : Command

data class RegisterUserCommandV1(val command: RegisterUser.V1) : ApplicationCommand<RegisterUserCommandV1.Result>, RegisterUser by command {

    sealed interface Result {

        data object Accepted : Result

        sealed interface Rejected : Result {

            data class EmailAddressAlreadyInUse(val userId: SortableTimestampedUniqueIdentifier<*>) : Rejected
        }
    }
}

fun RegisterUser.V1.asApplicationCommand() = RegisterUserCommandV1(this)

interface RegisterUser : Command {

    val emailAddress: EmailAddress

    companion object {
        val typeName = Name("register-user")
    }

    class V1(override val emailAddress: EmailAddress, override val id: SortableTimestampedUniqueIdentifier<*>, override val timestamp: Instant) : RegisterUser {

        override val type: Command.Type get() = Type

        object Type : Command.Type {
            override val version = IntVersion(1)
            override val id = typeName
        }

        companion object
    }
}

interface Command : Instruction { // TODO add context

    override val type: Type

    companion object

    interface Type : Happening.Type {

        companion object
    }
}

interface Query : Instruction {

    override val type: Type

    companion object

    interface Type : Happening.Type {

        companion object
    }
}

interface Event : Happening {

    override val type: Type

    companion object

    interface Type : Happening.Type {

        companion object
    }
}

interface Instruction : Happening {

    companion object
}

interface Happening : Identifiable<SortableTimestampedUniqueIdentifier<*>>, Timestamped, Versioned<IntVersion> {

    val type: Type
    override val version: IntVersion get() = type.version

    companion object

    interface Type : Identifiable<Name>, Versioned<IntVersion> {

        companion object
    }
}

// TODO move
private val newULID = UniqueIdFactory().ulid
private val clock: Clock = Clock.System