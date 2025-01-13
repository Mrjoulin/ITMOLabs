package com.joulin.killerservice.config

import com.joulin.killerservice.exceptions.DetailSoapFaultDefinitionExceptionResolver
import com.joulin.killerservice.exceptions.ServiceFaultException
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.config.annotation.EnableWs
import org.springframework.ws.config.annotation.WsConfigurerAdapter
import org.springframework.ws.server.EndpointInterceptor
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor
import org.springframework.ws.transport.http.MessageDispatcherServlet
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition
import org.springframework.xml.xsd.SimpleXsdSchema
import org.springframework.xml.xsd.XsdSchema
import java.util.*


@Configuration
@EnableWs
class WebServiceConfig : WsConfigurerAdapter() {
    companion object {
        const val NAMESPACE_URI = "http://com/joulin/killerservice/dto"
    }

    @Bean
    fun exceptionResolver(): SoapFaultMappingExceptionResolver {
        val exceptionResolver: SoapFaultMappingExceptionResolver = DetailSoapFaultDefinitionExceptionResolver()
        val faultDefinition = SoapFaultDefinition()
        faultDefinition.faultCode = SoapFaultDefinition.SERVER
        exceptionResolver.setDefaultFault(faultDefinition)
        val errorMappings = Properties()
        errorMappings.setProperty(Exception::class.java.getName(), SoapFaultDefinition.SERVER.toString())
        errorMappings.setProperty(ServiceFaultException::class.java.getName(), SoapFaultDefinition.SERVER.toString())
        exceptionResolver.setExceptionMappings(errorMappings)
        exceptionResolver.order = 1
        return exceptionResolver
    }

    @Bean
    fun messageDispatcherServlet(context: ApplicationContext): ServletRegistrationBean<MessageDispatcherServlet> {
        val servlet = MessageDispatcherServlet()
        servlet.setApplicationContext(context)
        servlet.isTransformWsdlLocations = true
        return ServletRegistrationBean(servlet, "/ws/*")
    }

    @Bean(name = ["schema"])
    fun defaultWsdl11Definition(schema: XsdSchema?): DefaultWsdl11Definition {
        val wsdl11Definition = DefaultWsdl11Definition()
        wsdl11Definition.setPortTypeName("SchemaPort")
        wsdl11Definition.setLocationUri("/ws")
        wsdl11Definition.setTargetNamespace(NAMESPACE_URI)
        wsdl11Definition.setSchema(schema)
        return wsdl11Definition
    }

    @Bean
    fun xsdSchema(): XsdSchema {
        return SimpleXsdSchema(ClassPathResource("schema.xsd"))
    }

    override fun addInterceptors(interceptors: MutableList<EndpointInterceptor?>) {
        interceptors.add(payloadValidatingInterceptor())
    }

    @Bean
    fun payloadValidatingInterceptor(): PayloadValidatingInterceptor {
        val validatingInterceptor = PayloadValidatingInterceptor()
        validatingInterceptor.setSchema(ClassPathResource("schema.xsd"))
        validatingInterceptor.setValidateRequest(true)
        validatingInterceptor.setValidateResponse(true)
        return validatingInterceptor
    }
}