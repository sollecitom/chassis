package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.ddd.domain.Command

interface RegisterUser : Command {

    val emailAddress: EmailAddress

    companion object {
        val typeName = "register-user".let(::Name)
    }

    class V1(override val emailAddress: EmailAddress) : RegisterUser {

        override val type: Command.Type get() = Type

        object Type : Command.Type {
            override val version = 1.let(::IntVersion)
            override val id = typeName
        }

        companion object
    }
}