package org.sollecitom.chassis.example.webapp.vaadin.adapters.driving.web

import com.vaadin.flow.component.dependency.NpmPackage
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import org.springframework.web.context.annotation.ApplicationScope

@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
@Theme("flowcrmtutorial")
@PWA(name = "VaadinCRM", shortName = "CRM", offlinePath = "offline.html", offlineResources = ["./images/offline.png"])
@ApplicationScope // TODO try to remove this
class VaadinAppConfigurator : AppShellConfigurator

