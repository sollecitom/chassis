package org.sollecitom.chassis.ddd.test.utils

import com.element.dpg.libs.chassis.core.utils.TimeGenerator
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.create
import org.sollecitom.chassis.ddd.domain.Event

context(UniqueIdGenerator, TimeGenerator)
fun Event.Context.Companion.create(invocation: InvocationContext<*> = InvocationContext.create(), parent: Event.Reference? = null, originating: Event.Reference? = null): Event.Context = Event.Context(invocation = invocation, parent = parent, originating = originating)