package com.element.dpg.libs.chassis.correlation.core.test.utils.access.authorization

import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.Roles

fun AuthorizationPrincipal.Companion.create(roles: Roles = TestRoles.default): AuthorizationPrincipal = AuthorizationPrincipal(roles)

fun AuthorizationPrincipal.Companion.withoutRoles(): AuthorizationPrincipal = AuthorizationPrincipal(TestRoles.none)