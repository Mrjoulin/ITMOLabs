package com.joulin.killerservice.exceptions

import com.joulin.killerservice.dto.ServiceFaultDto
import org.springframework.ws.soap.SoapFault
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver
import javax.xml.namespace.QName

class DetailSoapFaultDefinitionExceptionResolver : SoapFaultMappingExceptionResolver() {
    override fun customizeFault(endpoint: Any, ex: Exception, fault: SoapFault) {
        logger.info("Exception processed ", ex)
        if (ex is ServiceFaultException) {
            val serviceFault: ServiceFaultDto = ex.serviceFault
            val detail = fault.addFaultDetail()
            detail.addFaultDetailElement(CODE).addText(serviceFault.code.toString())
            detail.addFaultDetailElement(DESCRIPTION).addText(serviceFault.description)
        }
    }

    companion object {
        private val CODE = QName("code")
        private val DESCRIPTION = QName("description")
    }
}