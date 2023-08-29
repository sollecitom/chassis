package org.sollecitom.chassis.correlation.core.test.utils.access.origin

import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin

context(WithCoreGenerators)
fun Origin.Companion.create(ipAddress: IpAddress = IpAddress.V4.localhost): Origin = Origin(ipAddress)