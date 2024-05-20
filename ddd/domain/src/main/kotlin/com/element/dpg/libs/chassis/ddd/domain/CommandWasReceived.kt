package com.element.dpg.libs.chassis.ddd.domain

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion

sealed class CommandWasReceived<out COMMAND : Command<*, *>>(val command: COMMAND, override val id: Id, override val timestamp: Instant, override val context: Event.Context) : Event {

    val commandType get() = command.type

    override val type get() = Companion.type

    companion object {
        val type = Happening.Type("command-was-received".let(::Name), 1.let(::IntVersion))
    }
}