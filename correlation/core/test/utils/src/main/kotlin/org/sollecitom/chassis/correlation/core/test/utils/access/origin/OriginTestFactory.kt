package org.sollecitom.chassis.correlation.core.test.utils.access.origin

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.web.client.info.domain.*

context(CoreDataGenerator)
fun Origin.Companion.create(ipAddress: IpAddress = IpAddress.V4.localhost, clientInfo: ClientInfo = ClientInfo.create()): Origin = Origin(ipAddress, clientInfo)