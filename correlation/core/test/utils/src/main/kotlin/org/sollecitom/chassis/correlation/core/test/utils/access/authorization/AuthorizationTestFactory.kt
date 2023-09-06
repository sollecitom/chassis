package org.sollecitom.chassis.correlation.core.test.utils.access.authorization

import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles

context(CoreDataGenerator)
fun AuthorizationPrincipal.Companion.create(roles: Roles = TestRoles.default): AuthorizationPrincipal = AuthorizationPrincipal(roles)

context(CoreDataGenerator)
fun AuthorizationPrincipal.Companion.withoutRoles(): AuthorizationPrincipal = AuthorizationPrincipal(TestRoles.none)