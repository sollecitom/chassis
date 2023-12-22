package org.sollecitom.chassis.correlation.core.test.utils.access.scope

import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessContainer
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessScope

fun AccessScope.Companion.withContainerStack(vararg containers: AccessContainer): AccessScope = AccessScope(containers.toList())