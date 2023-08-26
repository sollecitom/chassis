package org.sollecitom.chassis.correlation.core.test.utils.authorization

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.authorization.Role
import org.sollecitom.chassis.correlation.core.domain.authorization.Roles

object TestRoles {
    val admin = "admin".let(::Name).let(::Role)
    val readOnlyUser = "read-only-user".let(::Name).let(::Role)
    val user = "user".let(::Name).let(::Role)
    val projectAdmin = "project-admin".let(::Name).let(::Role)

    val default = Roles(values = setOf(user))
    val none = Roles(values = emptySet())
}