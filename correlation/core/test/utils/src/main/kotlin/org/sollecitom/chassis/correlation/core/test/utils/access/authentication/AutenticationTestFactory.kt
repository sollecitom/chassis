package org.sollecitom.chassis.correlation.core.test.utils.access.authentication

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import kotlin.time.Duration.Companion.minutes

context(WithCoreGenerators)
fun Authentication.Token.Companion.create(validTo: Instant, id: StringId = newId.string(), validFrom: Instant? = null): Authentication.Token = Authentication.Token(id, validFrom, validTo)

context(WithCoreGenerators)
fun Authentication.Token.Companion.create(id: StringId = newId.string(), validTo: Instant? = null, validFrom: Instant): Authentication.Token = Authentication.Token(id, validFrom, validTo)

context(WithCoreGenerators)
fun Authentication.Token.Companion.create(validFrom: Instant, validTo: Instant, id: StringId = newId.string()): Authentication.Token = Authentication.Token(id, validFrom, validTo)

context(WithCoreGenerators)
fun Authentication.Token.Companion.create(timeNow: Instant = clock.now(), id: StringId = newId.string(), validFrom: Instant? = timeNow - 5.minutes, validTo: Instant? = timeNow + 25.minutes): Authentication.Token = Authentication.Token(id, validFrom, validTo)