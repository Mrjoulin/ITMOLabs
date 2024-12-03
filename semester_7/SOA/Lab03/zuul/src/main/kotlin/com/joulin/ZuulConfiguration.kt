package com.joulin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.HttpClients
import org.apache.http.client.HttpClient

import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient
import com.netflix.client.RetryHandler
import com.netflix.client.config.IClientConfig
import com.netflix.loadbalancer.ILoadBalancer
import org.springframework.cloud.netflix.ribbon.ServerIntrospector
import org.springframework.context.annotation.Import
import org.springframework.cloud.netflix.ribbon.apache.HttpClientRibbonConfiguration

@Configuration
@Import(HttpClientRibbonConfiguration::class)
open class ZuulConfiguration {
    @Bean
    fun httpClient(): HttpClient {
        println("--------- GET HttpClient ------------------")
        return HttpClients.custom()
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .build()
    }

    @Bean
    fun closableHttpClient(): CloseableHttpClient {
        println("--------- GET CloseableHttpClient ------------------")
        return HttpClients.custom()
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .build()
    }

    @Bean
    fun ribbonLoadBalancingHttpClient(
        @Autowired config: IClientConfig?, serverIntrospector: ServerIntrospector?,
        loadBalancer: ILoadBalancer?, retryHandler: RetryHandler?,
        httpClient: CloseableHttpClient?
    ): RibbonLoadBalancingHttpClient {
        println("--------- GET RibbonLoadBalancingHttpClient ------------------")
        val client = RibbonLoadBalancingHttpClientDisableSSL(
            config, serverIntrospector
        )
        client.loadBalancer = loadBalancer
        client.retryHandler = retryHandler
        return client
    }

    /*
    @Bean
    fun ribbonCommandFactory(loadBalancerClient: RibbonLoadBalancerClient): HttpClientRibbonCommandFactory {
        // Create an SSLContext that trusts all certificates
        val sslContext = SSLContextBuilder()
            .loadTrustMaterial(null, TrustSelfSignedStrategy()) // Trust all certificates
            .build()

        // Create an HttpClient that uses the SSLContext and disables hostname verification
        val httpClient = HttpClients.custom()
            .setSSLContext(sslContext)
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)  // Disable hostname verification
            .setConnectionManager(PoolingHttpClientConnectionManager())
            .build()

        // Use the custom HttpClient in the Ribbon command factory
        return HttpClientRibbonCommandFactory(loadBalancerClient, httpClient)
    }

    @Bean
    fun ribbonClientHttpRequestFactory(
        @RibbonClientName serviceName: String,
        config: IClientConfig,
        serverList: ServerList<Server>
    ): RibbonLoadBalancingHttpClient {
        // Create an SSLContext that trusts all certificates
        val sslContext: SSLContext = SSLContextBuilder()
            .loadTrustMaterial(TrustSelfSignedStrategy())  // Trust all certificates
            .build()

        // Create an HttpClient that uses the custom SSLContext and disables hostname verification
        val httpClient = HttpClients.custom()
            .setSSLContext(sslContext)
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)  // Disable hostname verification
            .build()

        // Create a RibbonLoadBalancingHttpClient with the custom HttpClient
        return RibbonLoadBalancingHttpClient(httpClient, config)
    }
     */
}