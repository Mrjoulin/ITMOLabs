package com.joulin.killerservice.dto

import com.joulin.killerservice.config.WebServiceConfig
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceFault", propOrder = [
    "code",
    "description"
])
data class ServiceFaultDto(
    @XmlElement(name = "code", required = true)
    val code: Int,
    @XmlElement(name = "description", required = true)
    val description: String
)