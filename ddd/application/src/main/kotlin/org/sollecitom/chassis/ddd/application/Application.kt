package org.sollecitom.chassis.ddd.application

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

interface Application {

    context(InvocationContext<ACCESS>)
    suspend operator fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>): RESULT

    companion object
}