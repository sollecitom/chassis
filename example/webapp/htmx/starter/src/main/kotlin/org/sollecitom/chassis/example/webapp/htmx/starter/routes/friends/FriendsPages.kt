package org.sollecitom.chassis.example.webapp.htmx.starter.routes.friends

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.sollecitom.chassis.example.webapp.htmx.starter.HTMX
import org.sollecitom.chassis.example.webapp.htmx.starter.hxGet
import org.sollecitom.chassis.example.webapp.htmx.starter.hxSwap
import org.sollecitom.chassis.example.webapp.htmx.starter.hxTarget

object FriendsPages {

    fun index(friends: List<String>): String {
        return HTMX {
            h1 { a { href = "/${FriendsEndpoint.path}"; +"My Friends" } } // nested `a` inside a `h1`. To insert a text inside any tag we use the + operator
            friends.forEach { friend ->
                h2 {
                    a {
                        href = "/${FriendsEndpoint.path}/$friend" // Sets cursor pointer and works without js
                        hxGet("/$friend")
                        hxSwap("outerHTML")
                        hxTarget("closest h2")
                        +friend
                    }
                }
            }
        }
    }

    fun profile(friend: String, characteristic: String): String {
        return createHTML().article {
            h2(classes = "your-css-class") {
                +friend
            }
            p {
                +"My friend $friend is smart and a good $characteristic"
            }
        }
    }
}