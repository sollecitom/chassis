package org.sollecitom.chassis.example.webapp.vaadin.starter

import org.sollecitom.chassis.example.webapp.vaadin.configuration.Packages
import org.sollecitom.chassis.example.webapp.vaadin.configuration.configureLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = [Packages.BASE])
private open class Starter

fun main(vararg args: String) {

    configureLogging()
    SpringApplication.run(Starter::class.java, *args)
}