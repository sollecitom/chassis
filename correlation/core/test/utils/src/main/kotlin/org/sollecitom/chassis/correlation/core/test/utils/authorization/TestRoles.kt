package org.sollecitom.chassis.correlation.core.test.utils.authorization

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.authorization.Role

object TestRoles {
    val admin = "admin".let(::Name).let(::Role)
    val readOnlyUser = "read-only-user".let(::Name).let(::Role)
    val user = "user".let(::Name).let(::Role)
}