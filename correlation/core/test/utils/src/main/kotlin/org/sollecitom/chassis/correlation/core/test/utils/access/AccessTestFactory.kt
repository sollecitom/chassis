package org.sollecitom.chassis.correlation.core.test.utils.access

import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessContainer
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessScope
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.direct
import org.sollecitom.chassis.correlation.core.test.utils.access.authorization.create
import org.sollecitom.chassis.correlation.core.test.utils.access.authorization.withoutRoles
import org.sollecitom.chassis.correlation.core.test.utils.access.origin.create
import org.sollecitom.chassis.correlation.core.test.utils.access.scope.create
import org.sollecitom.chassis.correlation.core.test.utils.access.scope.withContainerStack

context(UniqueIdGenerator)
fun Access.Companion.unauthenticated(origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.withoutRoles(), scope: AccessScope = AccessScope.withContainerStack(AccessContainer.create(), AccessContainer.create())): Access.Unauthenticated = Access.Unauthenticated(origin, authorization, scope)

context(UniqueIdGenerator)
fun Access.Unauthenticated.Companion.create(origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.withoutRoles(), scope: AccessScope = AccessScope.withContainerStack(AccessContainer.create())): Access.Unauthenticated = Access.Unauthenticated(origin, authorization, scope)

context(UniqueIdGenerator, TimeGenerator)
fun Access.Companion.authenticated(actor: Actor = Actor.direct(), origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.create(), scope: AccessScope = AccessScope.withContainerStack(AccessContainer.create(), AccessContainer.create())): Access.Authenticated = Access.Authenticated(actor, origin, authorization, scope)

context(UniqueIdGenerator, TimeGenerator)
fun Access.Authenticated.Companion.authenticated(actor: Actor = Actor.direct(), origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.create(), scope: AccessScope = AccessScope.withContainerStack(AccessContainer.create())): Access.Authenticated = Access.Authenticated(actor, origin, authorization, scope)