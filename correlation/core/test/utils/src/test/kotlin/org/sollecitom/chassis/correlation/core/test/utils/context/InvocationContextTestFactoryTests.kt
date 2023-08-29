package org.sollecitom.chassis.correlation.core.test.utils.context

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.direct
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.user
import org.sollecitom.chassis.correlation.core.test.utils.access.authenticated
import org.sollecitom.chassis.correlation.core.test.utils.access.unauthenticated
import org.sollecitom.chassis.correlation.core.test.utils.trace.create

@TestInstance(PER_CLASS)
private class InvocationContextTestFactoryExampleTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `customizing the trace`() {

        val actionId = newId.external()
        val trace = Trace.create(externalInvocationTrace = ExternalInvocationTrace.create(actionId = actionId))

        val context = InvocationContext.create(trace = { trace })

        assertThat(context.trace).isEqualTo(trace)
    }

    @Test
    fun `customizing the access`() {

        val access = Access.unauthenticated()

        val context = InvocationContext.create(access = { access })

        assertThat(context.access).isEqualTo(access)
    }

    @Test
    fun `deriving idempotency from access and trace`() {

        val invocationId = newId.external()
        val actorId = newId.internal()
        val tenantId = newId.internal()
        val trace = Trace.create(externalInvocationTrace = ExternalInvocationTrace.create(invocationId = invocationId))
        val access = Access.authenticated(actor = Actor.direct(account = Actor.Account.user(id = actorId, tenant = Tenant(id = tenantId))))
        val context = InvocationContext(access = access, trace = trace)

        val idempotency = context.idempotency

        assertThat(idempotency.namespace).isNotNull().isEqualTo("${tenantId.stringValue}-${actorId.stringValue}".let(::Name))
        assertThat(idempotency.key).isEqualTo(trace.external.invocationId.stringValue.let(::Name))
        assertThat(idempotency.id()).isEqualTo("${idempotency.namespace!!.value}-${idempotency.key.value}".let(::Name))
        assertThat(idempotency.id(separator = "*")).isEqualTo("${idempotency.namespace!!.value}*${idempotency.key.value}".let(::Name))
    }
}