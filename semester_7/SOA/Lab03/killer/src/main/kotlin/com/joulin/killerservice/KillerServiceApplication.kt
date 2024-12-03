package com.joulin.killerservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.ribbon.RibbonClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@SpringBootApplication
@EnableZuulProxy
@RibbonClient(name = "killer-service")
@EnableDiscoveryClient
private class KillerServiceApplication : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(KillerServiceApplication::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<KillerServiceApplication>(*args)
}