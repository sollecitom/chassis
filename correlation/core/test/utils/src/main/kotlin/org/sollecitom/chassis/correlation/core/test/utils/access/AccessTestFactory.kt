package org.sollecitom.chassis.correlation.core.test.utils.access

import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.direct
import org.sollecitom.chassis.correlation.core.test.utils.access.authorization.create
import org.sollecitom.chassis.correlation.core.test.utils.access.authorization.withoutRoles
import org.sollecitom.chassis.correlation.core.test.utils.access.origin.create

context(CoreDataGenerator)
fun Access.Companion.unauthenticated(origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.withoutRoles()): Access.Unauthenticated = Access.Unauthenticated(origin, authorization)

context(CoreDataGenerator)
fun Access.Unauthenticated.Companion.create(origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.withoutRoles()): Access.Unauthenticated = Access.Unauthenticated(origin, authorization)

context(CoreDataGenerator)
fun Access.Companion.authenticated(actor: Actor = Actor.direct(), origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.create()): Access.Authenticated = Access.Authenticated(actor, origin, authorization)

context(CoreDataGenerator)
fun Access.Authenticated.Companion.authenticated(actor: Actor = Actor.direct(), origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.create()): Access.Authenticated = Access.Authenticated(actor, origin, authorization)