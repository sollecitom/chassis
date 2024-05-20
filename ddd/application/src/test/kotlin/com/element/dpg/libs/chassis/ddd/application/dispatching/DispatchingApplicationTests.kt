package com.element.dpg.libs.chassis.ddd.application.dispatching

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.domain.versioning.IntVersion
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.core.test.utils.context.unauthenticated
import com.element.dpg.libs.chassis.ddd.application.dispatching.Application
import com.element.dpg.libs.chassis.ddd.application.dispatching.dispatching.CommandHandler
import com.element.dpg.libs.chassis.ddd.domain.Command
import com.element.dpg.libs.chassis.ddd.domain.Happening
import com.element.dpg.libs.chassis.test.utils.assertions.failedThrowing
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class DispatchingApplicationTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `dispatching the processing of a command to a specific handler`() = runTest {

        val command = SomeCommand(intensity = 50)
        val arrangedResult = SomeCommand.Result.Successful(intensity = 88)
        val handler = SomeCommandHandler(stubbedResult = arrangedResult)
        val application = Application(handler)
        val invocationContext = InvocationContext.unauthenticated()

        val result = with(invocationContext) { application(command) }

        assertThat(result).isEqualTo(arrangedResult)
        assertThat(handler.capturedCommand()).isEqualTo(command)
        assertThat(handler.capturedInvocationContext()).isEqualTo(invocationContext)
    }

    @Test
    fun `attempting to process a command for which there is no specific handler`() = runTest {

        val command = SomeCommand(intensity = 50)
        val application = Application()
        val invocationContext = InvocationContext.unauthenticated()

        val result = runCatching { with(invocationContext) { application(command) } }

        assertThat(result).failedThrowing<IllegalStateException>()
    }
}

private class SomeCommandHandler(private val stubbedResult: SomeCommand.Result) : CommandHandler<SomeCommand, SomeCommand.Result, Access.Unauthenticated> {

    override val commandType: Happening.Type get() = SomeCommand.type

    private lateinit var capturedCommand: SomeCommand
    private lateinit var capturedInvocationContext: InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated>

    context(InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated>)
    override suspend fun process(command: SomeCommand): SomeCommand.Result {

        this.capturedCommand = command
        this.capturedInvocationContext = this@InvocationContext
        return stubbedResult
    }

    fun capturedCommand() = capturedCommand
    fun capturedInvocationContext() = capturedInvocationContext
}

private data class SomeCommand(val intensity: Int) : Command<SomeCommand.Result, _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated> {

    override val accessRequirements get() = Command.AccessRequirements.None
    override val type: Happening.Type get() = Companion.type

    sealed interface Result {

        data class Successful(val intensity: Int) : Result
    }

    companion object {
        val type = Happening.Type("some-command".let(::Name), IntVersion(1))
    }
}