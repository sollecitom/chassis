package org.sollecitom.chassis.correlation.core.test.utils.access.authentication

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.access.authentication.CredentialsBasedAuthentication
import org.sollecitom.chassis.correlation.core.domain.access.authentication.FederatedAuthentication
import org.sollecitom.chassis.correlation.core.domain.access.authentication.StatelessAuthentication
import org.sollecitom.chassis.correlation.core.domain.access.session.FederatedSession
import org.sollecitom.chassis.correlation.core.domain.access.session.Session
import org.sollecitom.chassis.correlation.core.domain.access.session.SimpleSession
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.access.session.federated
import org.sollecitom.chassis.correlation.core.test.utils.access.session.simple
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import kotlin.time.Duration.Companion.minutes

context(WithCoreGenerators)
fun Authentication.Token.Companion.create(validTo: Instant, id: Id = newId.external(), validFrom: Instant? = null): Authentication.Token = Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)

context(WithCoreGenerators)
fun Authentication.Token.Companion.create(id: Id = newId.external(), validTo: Instant? = null, validFrom: Instant): Authentication.Token = Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)

context(WithCoreGenerators)
fun Authentication.Token.Companion.create(validFrom: Instant, validTo: Instant, id: Id = newId.external()): Authentication.Token = Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)

context(WithCoreGenerators)
fun Authentication.Token.Companion.create(timeNow: Instant = clock.now(), id: Id = newId.external(), validFrom: Instant? = timeNow - 5.minutes, validTo: Instant? = timeNow + 25.minutes): Authentication.Token = Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)

context(WithCoreGenerators)
fun Authentication.Companion.credentialsBased(timeNow: Instant = clock.now(), token: Authentication.Token = Authentication.Token.create(timeNow = timeNow), session: SimpleSession = Session.simple()): CredentialsBasedAuthentication = CredentialsBasedAuthentication(token = token, session = session)

context(WithCoreGenerators)
fun Authentication.Companion.federated(timeNow: Instant = clock.now(), token: Authentication.Token = Authentication.Token.create(timeNow = timeNow), session: FederatedSession = Session.federated()): FederatedAuthentication = FederatedAuthentication(token = token, session = session)

context(WithCoreGenerators)
fun Authentication.Companion.federated(timeNow: Instant = clock.now(), tenant: Tenant = Tenant.create(), token: Authentication.Token = Authentication.Token.create(timeNow = timeNow), session: FederatedSession = Session.federated(tenant = tenant)): FederatedAuthentication = FederatedAuthentication(token = token, session = session)

context(WithCoreGenerators)
fun Authentication.Companion.stateless(timeNow: Instant = clock.now(), token: Authentication.Token = Authentication.Token.create(timeNow = timeNow)): StatelessAuthentication = StatelessAuthentication(token = token)