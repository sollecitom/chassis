package org.sollecitom.chassis.correlation.core.serialization.json.access.origin

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.access.origin.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification
import org.sollecitom.chassis.test.utils.params.ParameterizedTestSupport

@TestInstance(PER_CLASS)
private class OriginJsonSerializationTests : JsonSerdeTestSpecification<Origin>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override val jsonSerde get() = Origin.jsonSerde

    override fun arguments() = ParameterizedTestSupport.arguments(
        "with-V4-ip-address" to Origin.create(ipAddress = IpAddress.create("127.0.0.1")),
        "with-V6-ip-address" to Origin.create(ipAddress = IpAddress.create("::1"))
    )
}