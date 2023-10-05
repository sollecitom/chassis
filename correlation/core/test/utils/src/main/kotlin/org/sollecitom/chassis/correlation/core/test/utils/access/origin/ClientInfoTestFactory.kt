package org.sollecitom.chassis.correlation.core.test.utils.access.origin

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.web.client.info.domain.*

context(CoreDataGenerator)
fun ClientInfo.Companion.create(device: Device = Device.create(), operatingSystem: OperatingSystem = OperatingSystem.create(), layoutEngine: LayoutEngine = LayoutEngine.create(), agent: Agent = Agent.create()): ClientInfo = ClientInfo(device, operatingSystem, layoutEngine, agent)

context(CoreDataGenerator)
fun Device.Companion.create(className: String? = "Mobile", name: String? = "Pixel 8", brand: String? = "Google"): Device = Device(className?.let(::Name), name?.let(::Name), brand?.let(::Name))

context(CoreDataGenerator)
fun OperatingSystem.Companion.create(className: String? = "Android", name: String? = "Android (Upside Down Cake)", version: String? = "14"): OperatingSystem = OperatingSystem(className?.let(::Name), name?.let(::Name), version?.let(::Name)?.let(Version.Companion::parse))

context(CoreDataGenerator)
fun LayoutEngine.Companion.create(className: String? = "WebKit", name: String? = "AppleWebKit", version: String? = "800.6.25"): LayoutEngine = LayoutEngine(className?.let(::Name), name?.let(::Name), version?.let(::Name)?.let(Version.Companion::parse))

context(CoreDataGenerator)
fun Agent.Companion.create(className: String? = "Special", name: String? = "Apache-HttpAsyncClient", version: String? = "5.2.1"): Agent = Agent(className?.let(::Name), name?.let(::Name), version?.let(::Name)?.let(Version.Companion::parse))