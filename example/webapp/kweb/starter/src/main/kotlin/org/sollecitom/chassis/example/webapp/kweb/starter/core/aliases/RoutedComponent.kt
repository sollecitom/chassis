package org.sollecitom.chassis.example.webapp.kweb.starter.core.aliases

import kweb.ElementCreator
import kweb.state.KVar

typealias RoutedComponent = (ElementCreator<*>, RoutingParams) -> Unit