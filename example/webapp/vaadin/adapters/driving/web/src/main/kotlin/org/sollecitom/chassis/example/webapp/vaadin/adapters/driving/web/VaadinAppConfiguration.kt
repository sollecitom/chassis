package org.sollecitom.chassis.example.webapp.vaadin.adapters.driving.web

import com.vaadin.flow.component.dependency.NpmPackage
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.spring.annotation.EnableVaadin
import com.vaadin.flow.theme.Theme
import org.springframework.context.annotation.Configuration

@NpmPackage(value = "lumo-css-framework", version = "^4.0.10") // TODO fix this
@Theme("flowcrmtutorial")
@PWA(name = "VaadinCRM", shortName = "CRM", offlinePath = "offline.html", offlineResources = ["./images/offline.png"])
@Configuration
@EnableVaadin("org.sollecitom.chassis.example.webapp.vaadin.adapters.driving.web")
open class VaadinAppConfiguration : AppShellConfigurator