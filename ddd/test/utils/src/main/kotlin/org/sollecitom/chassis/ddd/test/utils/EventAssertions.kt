package org.sollecitom.chassis.ddd.test.utils

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.Event

fun Assert<Event>.hasInvocationContext(context: InvocationContext<*>) = given { event ->

    assertThat(event.context.invocation).isEqualTo(context)
}

fun Assert<Event>.isOriginating() = given { event ->

    assertThat(event.context.parent).isNull()
    assertThat(event.context.originating).isNull()
}

fun Assert<Event>.descendsFrom(parent: Event) = given { event ->

    assertThat(event.context.parent).isEqualTo(parent.reference)
}

fun Assert<Event>.originatesFrom(originating: Event) = given { event ->

    assertThat(event.context.originating).isEqualTo(originating.reference)
}

fun <COMMAND : Command<*, *>> Assert<CommandWasReceived<COMMAND>>.hasCommandAndContext(command: COMMAND, context: InvocationContext<Access>) = given { event ->

    assertThat(event.command).isEqualTo(command)
    assertThat(event).hasInvocationContext(context)
}