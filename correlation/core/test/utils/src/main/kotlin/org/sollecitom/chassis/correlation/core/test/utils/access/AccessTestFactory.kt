package org.sollecitom.chassis.correlation.core.test.utils.access

import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.origin.create

context(WithCoreGenerators)
fun Access.Companion.unauthenticated(origin: Origin = Origin.create()): Access.Unauthenticated<ULID, Authentication> = Access.Unauthenticated(origin)