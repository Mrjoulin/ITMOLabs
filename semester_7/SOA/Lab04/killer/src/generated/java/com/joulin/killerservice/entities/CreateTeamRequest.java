
package com.joulin.killerservice.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="teamName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="teamSize" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="startCaveId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
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
    "teamName",
    "teamSize",
    "startCaveId"
})
@XmlRootElement(name = "TeamRequest")
public class CreateTeamRequest {

    @XmlElement(required = true)
    protected String teamName;
    protected long teamSize;
    protected long startCaveId;

    /**
     * Gets the value of the teamName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Sets the value of the teamName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeamName(String value) {
        this.teamName = value;
    }

    /**
     * Gets the value of the teamSize property.
     * 
     */
    public long getTeamSize() {
        return teamSize;
    }

    /**
     * Sets the value of the teamSize property.
     * 
     */
    public void setTeamSize(long value) {
        this.teamSize = value;
    }

    /**
     * Gets the value of the startCaveId property.
     * 
     */
    public long getStartCaveId() {
        return startCaveId;
    }

    /**
     * Sets the value of the startCaveId property.
     * 
     */
    public void setStartCaveId(long value) {
        this.startCaveId = value;
    }

}
