package org.sollecitom.chassis.correlation.core.test.utils.authorization

import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.authorization.AuthorizationInfo
import org.sollecitom.chassis.correlation.core.domain.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.authorization.Roles

context(WithCoreGenerators)
fun AuthorizationPrincipal.Companion.create(roles: Roles = TestRoles.default): AuthorizationPrincipal = AuthorizationInfo(roles)

context(WithCoreGenerators)
fun AuthorizationPrincipal.Companion.withoutRoles(): AuthorizationPrincipal = AuthorizationInfo(TestRoles.none)