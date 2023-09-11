package org.sollecitom.chassis.example.write_endpoint.application

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.ddd.domain.Command

data class GenericCommandType(override val name: Name, override val version: IntVersion) : Command.Type