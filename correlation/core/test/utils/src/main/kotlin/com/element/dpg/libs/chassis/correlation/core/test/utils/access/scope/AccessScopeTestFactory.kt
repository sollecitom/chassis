package com.element.dpg.libs.chassis.correlation.core.test.utils.access.scope

import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessContainer
import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessScope

fun AccessScope.Companion.withContainerStack(vararg containers: AccessContainer): AccessScope = AccessScope(containers.toList())