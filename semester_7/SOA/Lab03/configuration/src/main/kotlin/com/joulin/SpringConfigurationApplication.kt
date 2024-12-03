package com.joulin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.config.server.EnableConfigServer


@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
private class SpringConfigurationApplication : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(SpringConfigurationApplication::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<SpringConfigurationApplication>(*args)
}