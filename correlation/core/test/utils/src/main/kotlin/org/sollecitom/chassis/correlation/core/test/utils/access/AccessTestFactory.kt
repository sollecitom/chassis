package org.sollecitom.chassis.correlation.core.test.utils.access

import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.direct
import org.sollecitom.chassis.correlation.core.test.utils.authorization.create
import org.sollecitom.chassis.correlation.core.test.utils.authorization.withoutRoles
import org.sollecitom.chassis.correlation.core.test.utils.origin.create

context(WithCoreGenerators)
fun Access.Companion.unauthenticated(origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.withoutRoles()): Access.Unauthenticated = Access.Unauthenticated(origin, authorization)

context(WithCoreGenerators)
fun Access.Unauthenticated.Companion.create(origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.withoutRoles()): Access.Unauthenticated = Access.Unauthenticated(origin, authorization)

context(WithCoreGenerators)
fun Access.Companion.authenticated(actor: Actor = Actor.direct(), origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.create()): Access.Authenticated = Access.Authenticated(actor, origin, authorization)

context(WithCoreGenerators)
fun Access.Authenticated.Companion.authenticated(actor: Actor = Actor.direct(), origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.create()): Access.Authenticated = Access.Authenticated(actor, origin, authorization)