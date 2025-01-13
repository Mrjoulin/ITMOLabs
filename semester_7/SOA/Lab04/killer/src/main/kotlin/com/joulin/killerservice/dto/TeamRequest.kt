package com.joulin.killerservice.dto

import com.joulin.killerservice.config.WebServiceConfig.Companion.NAMESPACE_URI
import com.joulin.killerservice.core.Team
import com.joulin.killerservice.exceptions.ServiceFaultException
import java.time.LocalDateTime
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlType


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = ["teamName", "teamSize", "startCaveId"])
@XmlRootElement(name = "TeamRequest", namespace = NAMESPACE_URI)
class TeamRequest {
    @XmlElement(name = "teamName", required = true, namespace = NAMESPACE_URI)
    var teamName: String? = null
    @XmlElement(name = "teamSize", required = true, namespace = NAMESPACE_URI)
    var teamSize: Long? = null
    @XmlElement(name = "startCaveId", required = true, namespace = NAMESPACE_URI)
    var startCaveId: Long? = null

    fun validateTeam(): Team {
        if (teamSize == null || teamSize!! <= 0)
            throw ServiceFaultException(ServiceFaultDto(400,"Team Size must be positive integer"))

        return Team(
            creationDate = LocalDateTime.now(),
            name= teamName!!,
            size = teamSize!!,
            caveId = startCaveId!!
        )
    }
}
