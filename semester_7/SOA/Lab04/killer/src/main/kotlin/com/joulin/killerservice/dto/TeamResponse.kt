package com.joulin.killerservice.dto

import com.joulin.killerservice.config.WebServiceConfig
import com.joulin.killerservice.core.Team
import jakarta.xml.bind.annotation.*
import java.time.format.DateTimeFormatter

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = ["id", "creationDate", "name", "size", "caveId"])
@XmlRootElement(name = "TeamResponse", namespace = WebServiceConfig.NAMESPACE_URI)
class TeamResponse {
    @XmlElement(name = "id", required = true, namespace = WebServiceConfig.NAMESPACE_URI)
    var id: Long = 0
    @XmlElement(name = "creationDate", required = true, namespace = WebServiceConfig.NAMESPACE_URI)
    var creationDate: String = ""
    @XmlElement(name = "name", required = true, namespace = WebServiceConfig.NAMESPACE_URI)
    var name: String = ""
    @XmlElement(name = "size", required = true, namespace = WebServiceConfig.NAMESPACE_URI)
    var size: Long = 0
    @XmlElement(name = "caveId", required = true, namespace = WebServiceConfig.NAMESPACE_URI)
    var caveId: Long = 0

    companion object {
        fun fromTeam(team: Team): TeamResponse {
            val teamResponse = TeamResponse()
            teamResponse.id = team.id
            teamResponse.creationDate = team.creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            teamResponse.name = team.name
            teamResponse.size = team.size
            teamResponse.caveId = team.caveId
            return teamResponse
        }
    }
}
