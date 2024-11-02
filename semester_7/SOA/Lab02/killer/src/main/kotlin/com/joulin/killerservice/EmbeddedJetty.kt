package com.joulin.killerservice

import jakarta.servlet.DispatcherType
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.filter.DelegatingFilterProxy
import org.springframework.web.servlet.DispatcherServlet
import java.io.IOException
import java.util.*


class EmbeddedJetty {
    @Throws(Exception::class)
    private fun startJetty(port: Int) {
        logger.info("Starting server at port {}", port)
        val server = Server(port)
        server.setHandler(getServletContextHandler(context))
        server.start()
        logger.info("Server started at port {}", port)
        server.join()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(EmbeddedJetty::class.java)
        private const val DEFAULT_PORT = 8080
        private const val CONTEXT_PATH = "/"
        private const val CONFIG_LOCATION = "com.joulin.killerservice.config"
        private const val MAPPING_URL = "/*"
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            EmbeddedJetty().startJetty(getPortFromArgs(args))
        }

        private fun getPortFromArgs(args: Array<String>): Int {
            if (args.isNotEmpty()) {
                try {
                    return args[0].toInt()
                } catch (ignore: NumberFormatException) {
                }
            }
            logger.info("No server port configured, falling back to {}", DEFAULT_PORT)
            return DEFAULT_PORT
        }

        @Throws(IOException::class)
        private fun getServletContextHandler(context: WebApplicationContext): ServletContextHandler {
            val contextHandler = ServletContextHandler()
            contextHandler.setErrorHandler(null)
            contextHandler.setContextPath(CONTEXT_PATH)
            contextHandler.addServlet(ServletHolder(DispatcherServlet(context)), MAPPING_URL)
            contextHandler.addEventListener(ContextLoaderListener(context))
            contextHandler.addFilter(
                FilterHolder(DelegatingFilterProxy("springSecurityFilterChain", context)),
                "/*", EnumSet.allOf(DispatcherType::class.java)
            )
            contextHandler.resourceBase = ClassPathResource("webapp").getURI().toString()
            return contextHandler
        }

        private val context: WebApplicationContext
            get() {
                val context = AnnotationConfigWebApplicationContext()
                context.setConfigLocation(CONFIG_LOCATION)
                return context
            }
    }
}