package com.element.dpg.libs.chassis.ddd.domain

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

interface CommandResultSubscriber : Startable, Stoppable {

    context(InvocationContext<ACCESS>)
    fun <COMMAND : Command<RESULT, ACCESS>, RESULT : Any, ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT>
}