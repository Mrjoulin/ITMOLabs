
package com.joulin.killerservice.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="teamId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="caveId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "teamId",
    "caveId"
})
@XmlRootElement(name = "MoveTeamRequest")
public class MoveTeamRequest {

    protected long teamId;
    protected long caveId;

    /**
     * Gets the value of the teamId property.
     * 
     */
    public long getTeamId() {
        return teamId;
    }

    /**
     * Sets the value of the teamId property.
     * 
     */
    public void setTeamId(long value) {
        this.teamId = value;
    }

    /**
     * Gets the value of the caveId property.
     * 
     */
    public long getCaveId() {
        return caveId;
    }

    /**
     * Sets the value of the caveId property.
     * 
     */
    public void setCaveId(long value) {
        this.caveId = value;
    }

}
