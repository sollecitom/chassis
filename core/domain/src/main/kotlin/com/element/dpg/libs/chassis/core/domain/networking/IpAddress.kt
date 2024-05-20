package com.element.dpg.libs.chassis.core.domain.networking

import com.google.common.net.InetAddresses
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

sealed interface IpAddress {

    val value: InetAddress
    val stringValue: String get() = value.hostAddress

    @JvmInline
    value class V4(override val value: Inet4Address) : IpAddress {

        companion object {
            val localhost: V4 = create("127.0.0.1") as V4
        }
    }

    @JvmInline
    value class V6(override val value: Inet6Address) : IpAddress {

        companion object {
            val localhost: V6 = create("::1") as V6
        }
    }

    companion object {

        fun create(rawIpAddress: String): IpAddress = when (val ipAddress = InetAddresses.forString(rawIpAddress)) {
            is Inet4Address -> V4(ipAddress)
            else -> V6(ipAddress as Inet6Address)
        }
    }
}