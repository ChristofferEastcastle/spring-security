package com.example.security.configs

import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@Component
class ServerPortCustomizer: WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    override fun customize(factory: ConfigurableWebServerFactory) {
        val portEnv = System.getenv()["PORT"]

        portEnv?.let { factory.setPort(portEnv.toInt()) }
    }
}