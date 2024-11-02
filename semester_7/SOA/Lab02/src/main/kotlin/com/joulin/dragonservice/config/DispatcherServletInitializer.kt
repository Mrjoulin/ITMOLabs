package com.joulin.dragonservice.config

import org.springframework.lang.NonNull
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

open class DispatcherServletInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getRootConfigClasses(): Array<Class<*>>? {
        return null
    }

    override fun getServletConfigClasses(): Array<Class<*>> {
        return arrayOf(SpringConfig::class.java)
    }

    @NonNull
    override fun getServletMappings(): Array<String> {
        return arrayOf("/")
    }
}