package org.sollecitom.chassis.ddd.application

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

interface Application {

    suspend operator fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>, context: InvocationContext<ACCESS>): RESULT

    companion object
}