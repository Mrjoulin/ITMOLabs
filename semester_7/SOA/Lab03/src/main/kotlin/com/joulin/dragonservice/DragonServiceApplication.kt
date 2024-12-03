package com.joulin.dragonservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class DragonServiceApplication : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(DragonServiceApplication::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<DragonServiceApplication>(*args)
}