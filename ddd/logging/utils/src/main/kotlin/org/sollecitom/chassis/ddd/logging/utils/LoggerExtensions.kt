package org.sollecitom.chassis.ddd.logging.utils

import org.sollecitom.chassis.correlation.logging.utils.log
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.logger.core.Logger

context(Event.Context)
fun Logger.log(error: Throwable? = null, evaluateMessage: () -> String) = with(invocation) { log(error, evaluateMessage) }