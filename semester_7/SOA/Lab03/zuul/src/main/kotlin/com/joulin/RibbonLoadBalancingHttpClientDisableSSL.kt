package com.joulin

import com.netflix.client.config.IClientConfig
import org.apache.http.HttpResponse
import org.apache.http.client.config.RequestConfig
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.ribbon.RibbonProperties
import org.springframework.cloud.netflix.ribbon.ServerIntrospector
import org.springframework.cloud.netflix.ribbon.apache.HttpClientRibbonConfiguration
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpRequest
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpResponse
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@Import(HttpClientRibbonConfiguration::class)
class RibbonLoadBalancingHttpClientDisableSSL(
    config: IClientConfig?, serverIntrospector: ServerIntrospector?
) : RibbonLoadBalancingHttpClient(config, serverIntrospector) {

    override fun createDelegate(@Autowired config: IClientConfig?): CloseableHttpClient? {
        return HttpClients.custom()
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .build()
    }

    override fun execute(
        request: RibbonApacheHttpRequest,
        configOverride: IClientConfig?
    ): RibbonApacheHttpResponse {
        val config = configOverride ?: config
        val ribbon = RibbonProperties.from(config)
        val requestConfig = RequestConfig.custom()
            .setConnectTimeout(ribbon.connectTimeout(connectTimeout))
            .setSocketTimeout(ribbon.readTimeout(readTimeout))
            .setRedirectsEnabled(ribbon.isFollowRedirects(followRedirects))
            .setContentCompressionEnabled(ribbon.isGZipPayload(gzipPayload))
            .build()
        val newRequest = getSecureRequest(request, configOverride)
        val httpUriRequest = newRequest.toRequest(requestConfig)
        println(httpUriRequest)
        println("REQUEST TO ${httpUriRequest.uri}")
        val httpResponse: HttpResponse = delegate.execute(httpUriRequest)
        return RibbonApacheHttpResponse(httpResponse, httpUriRequest.uri)
    }
}