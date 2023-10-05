package org.sollecitom.chassis.web.client.info.analyzer

import nl.basjes.parse.useragent.AgentField
import nl.basjes.parse.useragent.UserAgent
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.web.client.info.domain.*

operator fun ClientInfoAnalyzer.Companion.invoke(): ClientInfoAnalyzer = YauaaClientInfoAnalyzer

private object YauaaClientInfoAnalyzer : ClientInfoAnalyzer {

    private val analyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withAllFields().build()

    override fun invoke(requestHeaders: Map<String, String>): ClientInfo {

        val userAgent = analyzer.parse(requestHeaders)
        if (userAgent.hasSyntaxError()) { /* TODO what to do in this case? */
        }
        return userAgent.adapted().data()
    }

    private fun UserAgent.ImmutableUserAgent.adapted(): YauaaClientInfoAdapter = YauaaClientInfoAdapter(this)

    private fun YauaaClientInfoAdapter.data(): ClientInfo = ClientInfo(device, operatingSystem, layoutEngine, agent)

    private data class YauaaClientInfoAdapter(private val userAgent: UserAgent.ImmutableUserAgent) {

        val device by lazy { Device(className = deviceClass.valueAsNameOrNull(), name = deviceName.valueAsNameOrNull(), brand = deviceBrand.valueAsNameOrNull()) }
        val operatingSystem by lazy { OperatingSystem(className = operatingSystemClass.valueAsNameOrNull(), name = operatingSystemName.valueAsNameOrNull(), version = operatingSystemVersion.valueAsVersionOrNull()) }
        val layoutEngine by lazy { LayoutEngine(className = layoutEngineClass.valueAsNameOrNull(), name = layoutEngineName.valueAsNameOrNull(), version = layoutEngineVersion.valueAsVersionOrNull()) }
        val agent by lazy { Agent(className = agentClass.valueAsNameOrNull(), name = agentName.valueAsNameOrNull(), version = agentVersion.valueAsVersionOrNull()) }

        private val AgentField.isUnknown: Boolean get() = confidence <= 0 || isDefaultValue

        private fun AgentField.valueAsNameOrNull(): Name? = takeUnless { it.isUnknown }?.let { value }?.let(::Name)

        private fun AgentField.valueAsVersionOrNull(): Version? = takeUnless { it.isUnknown }?.let { runCatching { Version.Semantic.parse(value) }.getOrElse { Version.Simple(value.let(::Name)) } }

        private val deviceClass get() = field(UserAgent.DEVICE_CLASS)
        private val deviceName get() = field(UserAgent.DEVICE_NAME)
        private val deviceBrand get() = field(UserAgent.DEVICE_BRAND)

        private val operatingSystemClass get() = field(UserAgent.OPERATING_SYSTEM_CLASS)
        private val operatingSystemName get() = field(UserAgent.OPERATING_SYSTEM_NAME)
        private val operatingSystemVersion get() = field(UserAgent.OPERATING_SYSTEM_VERSION)
        private val operatingSystemVersionMajor get() = field(UserAgent.OPERATING_SYSTEM_VERSION_MAJOR)
        private val operatingSystemNameVersion get() = field(UserAgent.OPERATING_SYSTEM_NAME_VERSION)
        private val operatingSystemNameVersionMajor get() = field(UserAgent.OPERATING_SYSTEM_NAME_VERSION_MAJOR)

        private val layoutEngineClass get() = field(UserAgent.LAYOUT_ENGINE_CLASS)
        private val layoutEngineName get() = field(UserAgent.LAYOUT_ENGINE_NAME)
        private val layoutEngineVersion get() = field(UserAgent.LAYOUT_ENGINE_VERSION)
        private val layoutEngineVersionMajor get() = field(UserAgent.LAYOUT_ENGINE_VERSION_MAJOR)
        private val layoutEngineNameVersion get() = field(UserAgent.LAYOUT_ENGINE_NAME_VERSION)
        private val layoutEngineNameVersionMajor get() = field(UserAgent.LAYOUT_ENGINE_NAME_VERSION_MAJOR)

        private val agentClass get() = field(UserAgent.AGENT_CLASS)
        private val agentName get() = field(UserAgent.AGENT_NAME)
        private val agentVersion get() = field(UserAgent.AGENT_VERSION)
        private val agentVersionMajor get() = field(UserAgent.AGENT_VERSION_MAJOR)
        private val agentNameVersion get() = field(UserAgent.AGENT_NAME_VERSION)
        private val agentNameVersionMajor get() = field(UserAgent.AGENT_NAME_VERSION_MAJOR)

        private fun field(name: String): AgentField = userAgent.get(name)

        data class Field(val name: String, val value: String, val confidence: Long, val isDefaultValue: Boolean) {

            val isUnknown get() = confidence <= 0 || isDefaultValue
        }
    }
}