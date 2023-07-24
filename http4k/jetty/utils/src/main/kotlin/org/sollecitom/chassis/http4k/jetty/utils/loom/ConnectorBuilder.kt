package org.sollecitom.chassis.http4k.jetty.utils.loom

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector

typealias ConnectorBuilder = (Server) -> ServerConnector