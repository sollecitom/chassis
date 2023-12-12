package org.sollecitom.chassis.pulsar.messaging.adapter
// // TODO implement
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
//import org.sollecitom.chassis.core.test.utils.testProvider
//import org.sollecitom.chassis.core.utils.CoreDataGenerator
//import org.sollecitom.chassis.logger.core.LoggingLevel
//import org.sollecitom.chassis.logging.standard.configuration.configureLogging
//import org.sollecitom.chassis.pulsar.test.utils.admin
//import org.sollecitom.chassis.pulsar.test.utils.client
//import org.sollecitom.chassis.pulsar.test.utils.create
//import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
//import org.sollecitom.chassis.pulsar.utils.PulsarTopic
//
//@TestInstance(PER_CLASS)
//private class PulsarMessagingTests : CoreDataGenerator by CoreDataGenerator.testProvider {
//
//    init {
//        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
//    }
//
//    override val pulsar = newPulsarContainer()
//    override val pulsarClient by lazy { pulsar.client() }
//    override val pulsarAdmin by lazy { pulsar.admin() }
//    override val topic = PulsarTopic.create()
//
//    @Test
//    fun `sending and receiving a single messages`() {
//
//
//    }
//}