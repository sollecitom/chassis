package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.correlation.core.domain.access.Access

interface Command<out RESULT, out ACCESS : Access> : Instruction {

    val requiresAuthentication: Boolean

    companion object
}