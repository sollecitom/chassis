package com.element.dpg.libs.chassis.correlation.core.test.utils.access.authorization

import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles

fun AuthorizationPrincipal.Companion.create(roles: Roles = TestRoles.default): AuthorizationPrincipal = AuthorizationPrincipal(roles)

fun AuthorizationPrincipal.Companion.withoutRoles(): AuthorizationPrincipal = AuthorizationPrincipal(TestRoles.none)