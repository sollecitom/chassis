package com.element.dpg.libs.chassis.ddd.domain

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.domain.versioning.IntVersion
import kotlinx.datetime.Instant

sealed class CommandWasReceived<out COMMAND : Command<*, *>>(val command: COMMAND, override val id: Id, override val timestamp: Instant, override val context: Event.Context) : Event {

    val commandType get() = command.type

    override val type get() = Companion.type

    companion object {
        val type = Happening.Type("command-was-received".let(::Name), 1.let(::IntVersion))
    }
}