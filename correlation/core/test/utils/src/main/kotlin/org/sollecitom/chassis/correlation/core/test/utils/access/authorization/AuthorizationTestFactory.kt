package org.sollecitom.chassis.correlation.core.test.utils.access.authorization

import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles

context(WithCoreGenerators)
fun AuthorizationPrincipal.Companion.create(roles: Roles = TestRoles.default): AuthorizationPrincipal = AuthorizationPrincipal(roles)

context(WithCoreGenerators)
fun AuthorizationPrincipal.Companion.withoutRoles(): AuthorizationPrincipal = AuthorizationPrincipal(TestRoles.none)