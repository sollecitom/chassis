package org.sollecitom.chassis.correlation.core.test.utils

import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.context.factory.Factory
import org.sollecitom.chassis.correlation.core.domain.context.factory.InvocationContextFactory

private class StandardInvocationContextTestFactory(private val delegate: InvocationContextFactory, coreGenerators: WithCoreGenerators) : InvocationContextTestFactory, WithCoreGenerators by coreGenerators, InvocationContextFactory by delegate

context(WithCoreGenerators)
val InvocationContext.Companion.testFactory: InvocationContextTestFactory
    get() = StandardInvocationContextTestFactory(InvocationContext.Factory, this@WithCoreGenerators)