package com.element.dpg.libs.chassis.correlation.core.test.utils.access.authorization

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.Role
import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.Roles

object TestRoles {
    val admin = "admin".let(::Name).let(::Role)
    val readOnlyUser = "read-only-user".let(::Name).let(::Role)
    val user = "user".let(::Name).let(::Role)
    val projectAdmin = "project-admin".let(::Name).let(::Role)

    val default = Roles(values = setOf(user))
    val none = Roles(values = emptySet())
}