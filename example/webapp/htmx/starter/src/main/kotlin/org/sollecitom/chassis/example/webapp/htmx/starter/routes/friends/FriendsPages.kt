package org.sollecitom.chassis.example.webapp.htmx.starter.routes.friends

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.sollecitom.chassis.example.webapp.htmx.starter.HTMX
import org.sollecitom.chassis.example.webapp.htmx.starter.hxGet
import org.sollecitom.chassis.example.webapp.htmx.starter.hxSwap
import org.sollecitom.chassis.example.webapp.htmx.starter.hxTarget

object FriendsPages {

    fun index(friends: List<String>) = HTMX {
        div {
            div {
                h1 { a { href = "/${FriendsEndpoint.path}"; +"My Friends" } }
            }
            div {
                id = "friends-div"
                table {
                    friends.forEach { friend ->
                        tr {
                            td {
                                a {
                                    href = "/${FriendsEndpoint.path}/$friend" // Sets cursor pointer and works without js
                                    hxGet("/${FriendsEndpoint.path}/$friend")
                                    hxSwap("outerHTML")
                                    hxTarget("#friends-div")
                                    +friend
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun profile(friend: String, characteristic: String) = createHTML().article {
        div {
            h2 {
                +friend
            }
            p {
                +"My friend $friend is smart and a good $characteristic"
            }
        }
    }
}