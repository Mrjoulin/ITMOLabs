package com.joulin.killerservice.dto

import com.joulin.killerservice.config.WebServiceConfig
import jakarta.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = ["teamId", "caveId"])
@XmlRootElement(name = "MoveTeamRequest", namespace = WebServiceConfig.NAMESPACE_URI)
class MoveTeamRequest {
    @XmlElement(name = "teamId", required = true, namespace = WebServiceConfig.NAMESPACE_URI)
    var teamId: Long = 0
    @XmlElement(name = "caveId", required = true, namespace = WebServiceConfig.NAMESPACE_URI)
    var caveId: Long = 0
}