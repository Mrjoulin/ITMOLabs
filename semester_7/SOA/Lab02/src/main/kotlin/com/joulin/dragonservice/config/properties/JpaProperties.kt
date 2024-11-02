package com.joulin.dragonservice.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JpaProperties {
    @Value("\${spring.jpa.show-sql}") // @Value("${spring.jpa.properties.hibernate.show_sql}")
    private val showSql: String? = null

    @Value("\${spring.jpa.hibernate.ddl-auto}") // @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
    private val ddlAuto: String? = null

    @Value("\${spring.jpa.properties.hibernate.physical_naming_strategy}")
    private val physicalNamingStrategy: String? = null
    fun get(): Properties {
        val properties = Properties()
        properties.setProperty("hibernate.show_sql", showSql)
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto)
        properties.setProperty("hibernate.physical_naming_strategy", physicalNamingStrategy)
        return properties
    }
}