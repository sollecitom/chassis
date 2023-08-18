package org.sollecitom.chassis.web.service.domain

import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable

interface WebService : WebInterface, Startable, Stoppable

