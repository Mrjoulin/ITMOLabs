package com.joulin.dragonservice.config

import com.joulin.dragonservice.config.properties.DataSourceProperties
import com.joulin.dragonservice.config.properties.JpaProperties
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.*
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.web.servlet.config.annotation.*


@Configuration
@EnableWebMvc
@RequiredArgsConstructor
class SpringConfig : WebMvcConfigurer {
    @Bean
    fun entityManagerFactory(
        dataSourceProperties: DataSourceProperties,
        jpaProperties: JpaProperties
    ): LocalContainerEntityManagerFactoryBean? {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(dataSourceProperties.driverClassName ?: "")
        dataSource.url = dataSourceProperties.url
        dataSource.username = dataSourceProperties.username
        dataSource.password = dataSourceProperties.password

        val factory = LocalContainerEntityManagerFactoryBean()

        factory.setDataSource(dataSource)
        factory.setPackagesToScan("com.joulin.dragonservice.core")
        factory.jpaVendorAdapter = HibernateJpaVendorAdapter()
        factory.setJpaProperties(jpaProperties.get())

        return factory
    }

    @Bean
    fun transactionManager(entityManagerFactory: LocalContainerEntityManagerFactoryBean): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory.`object`!!)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("http://localhost:3000", "https://localhost:8443", "https://localhost:9000/*", "https://localhost:8000/*")
            .allowedMethods("*")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/static/swagger-ui/")
            .resourceChain(false)
    }
}
