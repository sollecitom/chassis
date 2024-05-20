package com.element.dpg.libs.chassis.correlation.core.test.utils.access.origin

import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import com.element.dpg.libs.chassis.web.client.info.domain.ClientInfo

fun Origin.Companion.create(ipAddress: IpAddress = IpAddress.V4.localhost, clientInfo: ClientInfo = ClientInfo.create()): Origin = Origin(ipAddress, clientInfo)