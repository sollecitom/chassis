package com.element.dpg.libs.chassis.web.service.domain

import com.element.dpg.libs.chassis.core.domain.lifecycle.Startable
import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable

interface WebService : WithWebInterface, Startable, Stoppable