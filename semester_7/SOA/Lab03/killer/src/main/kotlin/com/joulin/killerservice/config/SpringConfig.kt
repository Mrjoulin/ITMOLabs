package com.joulin.killerservice.config

import com.joulin.killerservice.config.properties.DataSourceProperties
import com.joulin.killerservice.config.properties.JpaProperties
import lombok.RequiredArgsConstructor
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.ssl.SSLContextBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.*
import org.springframework.core.io.Resource
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.net.ssl.SSLContext


@Configuration
@EnableWebMvc
// @EnableLoadTimeWeaving
@RequiredArgsConstructor
class SpringConfig : WebMvcConfigurer {
    @Value("\${trust.store}")
    private lateinit var trustStore: Resource

    @Value("\${trust.store.password}")
    private lateinit var trustStorePassword: String

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
        factory.setPackagesToScan("com.joulin.killerservice.core")
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
            .allowedOriginPatterns("http://localhost:3000", "https://localhost:8443", "https://localhost:9000/*")
            .allowedMethods("*")
    }

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate {
        val sslContext: SSLContext = SSLContextBuilder()
            .loadTrustMaterial(trustStore.url, trustStorePassword.toCharArray()).build()
        val sslConFactory = SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)
        val cm: PoolingHttpClientConnectionManager? = PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(sslConFactory)
            .build()
        val httpClient: CloseableHttpClient = HttpClients.custom().setConnectionManager(cm).build()
        val requestFactory: ClientHttpRequestFactory = HttpComponentsClientHttpRequestFactory(httpClient)
        return RestTemplate(requestFactory)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/static/swagger-ui/")
            .resourceChain(false)
    }
}
