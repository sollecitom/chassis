package org.sollecitom.chassis.correlation.core.serialization.json.access.origin

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.IpAddress
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.access.origin.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class OriginJsonSerializationTests : JsonSerdeTestSpecification<Origin>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Origin.jsonSerde

    override fun parameterizedArguments() = listOf(
        "with-V4-ip-address" to Origin.create(ipAddress = IpAddress.create("127.0.0.1")),
        "with-V6-ip-address" to Origin.create(ipAddress = IpAddress.create("::1"))
    )
}