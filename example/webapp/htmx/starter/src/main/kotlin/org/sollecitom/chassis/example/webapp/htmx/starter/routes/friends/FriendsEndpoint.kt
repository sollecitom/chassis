package org.sollecitom.chassis.example.webapp.htmx.starter.routes.friends

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.sollecitom.chassis.http4k.server.utils.toSuspending

class FriendsEndpoint {

    private val friends = listOf("Joey", "Ross", "Chandler")
    private val characteristic = listOf("swimmer", "runner", "debater")

    fun routes() = routes(
            "/" bind GET toSuspending { _ -> Response(OK).body(FriendsPages.index(friends)) },
            *friends.map { friend -> "/$friend" bind GET toSuspending { _ -> Response(OK).body(FriendsPages.profile(friend, characteristic.random())) } }.toTypedArray()
    )

    companion object {
        val path = "friends"
    }
}