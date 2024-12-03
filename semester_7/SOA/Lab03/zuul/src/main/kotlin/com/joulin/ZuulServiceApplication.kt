package com.joulin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@SpringBootApplication(scanBasePackages=["com.netflix.client.config.IClientConfig"])
@EnableZuulProxy
@EnableDiscoveryClient
private open class ZuulServiceApplication : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(ZuulServiceApplication::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<ZuulServiceApplication>(*args)
}