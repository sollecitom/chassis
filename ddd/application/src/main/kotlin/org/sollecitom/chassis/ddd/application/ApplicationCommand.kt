package org.sollecitom.chassis.ddd.application

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.domain.Command

interface ApplicationCommand<out RESULT, out ACCESS : Access> : Command {

    val requiresAuthentication: Boolean

    companion object
}