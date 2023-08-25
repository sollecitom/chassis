package org.sollecitom.chassis.correlation.core.test.utils.access.session

import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.access.session.FederatedSession
import org.sollecitom.chassis.correlation.core.domain.access.session.Session
import org.sollecitom.chassis.correlation.core.domain.access.session.SimpleSession
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.access.idp.create
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create

context(WithCoreGenerators)
fun Session.Companion.simple(id: StringId = newId.string()): SimpleSession = SimpleSession(id)

context(WithCoreGenerators)
fun Session.Companion.federated(tenant: Tenant = Tenant.create(), id: StringId = newId.string(), identityProvider: IdentityProvider = IdentityProvider.create(tenant = tenant)): FederatedSession = FederatedSession(id, identityProvider)

context(WithCoreGenerators)
fun Session.Companion.federated(identityProvider: IdentityProvider, id: StringId = newId.string()): FederatedSession = FederatedSession(id, identityProvider)