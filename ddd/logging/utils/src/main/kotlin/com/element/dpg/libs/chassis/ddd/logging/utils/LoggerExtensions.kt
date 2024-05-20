package com.element.dpg.libs.chassis.ddd.logging.utils

import com.element.dpg.libs.chassis.correlation.logging.utils.log
import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.logger.core.Logger

context(Event.Context)
fun Logger.log(error: Throwable? = null, evaluateMessage: () -> String) = with(invocation) { log(error, evaluateMessage) }