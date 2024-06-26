package com.element.dpg.libs.chassis.correlation.core.test.utils.context

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.Actor
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.Toggles
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.invoke
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.withDefaultValue
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.withToggle
import com.element.dpg.libs.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import com.element.dpg.libs.chassis.correlation.core.domain.trace.Trace
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.actor.direct
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.actor.user
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authenticated
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.unauthenticated
import com.element.dpg.libs.chassis.correlation.core.test.utils.toggles.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.trace.create
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class InvocationContextTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

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
    fun `customizing the toggles`() {

        val invocationVisibility = Toggles.InvocationVisibility.withDefaultValue(InvocationVisibility.DEFAULT)
        val overriddenVisibility = InvocationVisibility.HIGH
        val toggles = Toggles().withToggle(Toggles.InvocationVisibility, overriddenVisibility)

        val context = InvocationContext.create(toggles = { toggles })
        val visibility = invocationVisibility.invoke(context)

        assertThat(context.toggles).isEqualTo(toggles)
        assertThat(visibility).isEqualTo(overriddenVisibility)
    }

    @Test
    fun `accessing the toggles when there are no overrides`() {

        val defaultValue = InvocationVisibility.DEFAULT
        val invocationVisibility = Toggles.InvocationVisibility.withDefaultValue(defaultValue)
        val toggles = Toggles()

        val context = InvocationContext.create(toggles = { toggles })
        val visibility = invocationVisibility(context)
        val visibilityOrNull = Toggles.InvocationVisibility(context)

        assertThat(context.toggles).isEqualTo(toggles)
        assertThat(visibility).isEqualTo(defaultValue)
        assertThat(visibilityOrNull).isNull()
    }

    @Test
    fun `deriving idempotency from access and trace`() {

        val invocationId = newId.external()
        val actorId = newId.internal()
        val customerId = newId.internal()
        val tenantId = newId.internal()
        val trace = Trace.create(externalInvocationTrace = ExternalInvocationTrace.create(invocationId = invocationId))
        val access = Access.authenticated(actor = Actor.direct(account = Actor.Account.user(id = actorId, customer = Customer(id = customerId), tenant = Tenant(id = tenantId))))
        val toggles = Toggles.create()
        val context = InvocationContext(access = access, trace = trace, toggles = toggles, specifiedTargetTenant = null)

        val idempotency = context.idempotency

        assertThat(idempotency.namespace).isNotNull().isEqualTo("${tenantId.stringValue}-${customerId.stringValue}-${actorId.stringValue}".let(::Name))
        assertThat(idempotency.key).isEqualTo(trace.external.invocationId.stringValue.let(::Name))
        assertThat(idempotency.id()).isEqualTo("${idempotency.namespace!!.value}-${idempotency.key.value}".let(::Name))
        assertThat(idempotency.id(separator = "*")).isEqualTo("${idempotency.namespace!!.value}*${idempotency.key.value}".let(::Name))
    }
}