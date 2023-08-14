package org.sollecitom.chassis.example.webapp.vaadin.adapters.driving.web.views

import org.springframework.stereotype.Component

@Component
class StubbedGreetingService : GreetingService {

    override fun greet(name: String): String = "Hello $name"
}