/**
 * NEXUSe2e Business Messaging Open Source  
 * Copyright 2007, Tamgroup and X-ioma GmbH   
 *  
 * This is free software; you can redistribute it and/or modify it  
 * under the terms of the GNU Lesser General Public License as  
 * published by the Free Software Foundation version 2.1 of  
 * the License.  
 *  
 * This software is distributed in the hope that it will be useful,  
 * but WITHOUT ANY WARRANTY; without even the implied warranty of  
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU  
 * Lesser General Public License for more details.  
 *  
 * You should have received a copy of the GNU Lesser General Public  
 * License along with this software; if not, write to the Free  
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.nexuse2e.pojo;

// Generated 02.11.2006 15:39:42 by Hibernate Tools 3.2.0.beta6a

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * PipeletPojo generated by hbm2java
 */
@XmlType(name = "PipeletType")
@XmlAccessorType(XmlAccessType.NONE)
public class PipeletPojo implements java.io.Serializable {

    /**
     * 
     */
    private static final long      serialVersionUID = 1602063679628056983L;

    // Fields    
    private int                    nxPipeletId;
    private PipelinePojo           pipeline;
    private ComponentPojo          component;
    private Date                   createdDate;
    private Date                   modifiedDate;
    private int                    modifiedNxUserId;
    private int                    position;
    private boolean                frontend;
    private boolean                forward          = true;
    private boolean                endpoint;
    private String                 name;
    private String                 description;
    @XmlElementWrapper(name = "PipeletParams")
    @XmlElement(name = "PipeletParam")
    private List<PipeletParamPojo> pipeletParams    = new Vector<PipeletParamPojo>( 0 );

    private int                    nxComponentId;
    
    // Constructors

    /** default constructor */
    public PipeletPojo() {

        createdDate = new Date();
        modifiedDate = createdDate;
    }

    /** minimal constructor */
    public PipeletPojo( PipelinePojo pipeline, ComponentPojo component, Date createdDate, Date modifiedDate,
            int modifiedNxUserId, int position, String name ) {

        this.pipeline = pipeline;
        this.component = component;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.position = position;
        this.name = name;
    }

    /** full constructor */
    public PipeletPojo( PipelinePojo pipeline, ComponentPojo component, Date createdDate, Date modifiedDate,
            int modifiedNxUserId, int position, String name, String description, List<PipeletParamPojo> pipeletParams ) {

        this.pipeline = pipeline;
        this.component = component;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.position = position;
        this.name = name;
        this.description = description;
        this.pipeletParams = pipeletParams;
    }

    // Property accessors
    @XmlAttribute
    public int getNxPipeletId() {

        return this.nxPipeletId;
    }

    public void setNxPipeletId( int nxPipeletId ) {

        this.nxPipeletId = nxPipeletId;
    }

    public PipelinePojo getPipeline() {

        return this.pipeline;
    }

    public void setPipeline( PipelinePojo pipeline ) {

        this.pipeline = pipeline;
    }

    /**
     * Required for JAXB 
     * @return
     */
    @XmlAttribute
    public int getNxComponentId() {

        if ( this.component != null ) {
            return this.component.getNxComponentId();

        }
        return nxComponentId;
    }
    
    /**
     * Required for JAXB 
     * @param componentId
     */
    public void setNxComponentId( int componentId ) {
        this.nxComponentId = componentId;
    }

    public ComponentPojo getComponent() {

        return this.component;
    }

    public void setComponent( ComponentPojo component ) {

        this.component = component;
    }

    public Date getCreatedDate() {

        return this.createdDate;
    }

    public void setCreatedDate( Date createdDate ) {

        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {

        return this.modifiedDate;
    }

    public void setModifiedDate( Date modifiedDate ) {

        this.modifiedDate = modifiedDate;
    }

    public int getModifiedNxUserId() {

        return this.modifiedNxUserId;
    }

    public void setModifiedNxUserId( int modifiedNxUserId ) {

        this.modifiedNxUserId = modifiedNxUserId;
    }

    @XmlAttribute
    public int getPosition() {

        return this.position;
    }

    public void setPosition( int position ) {

        this.position = position;
    }

    @XmlAttribute
    public String getName() {

        return this.name;
    }

    public void setName( String name ) {

        this.name = name;
    }

    @XmlAttribute
    public String getDescription() {

        return this.description;
    }

    public void setDescription( String description ) {

        this.description = description;
    }

    public List<PipeletParamPojo> getPipeletParams() {

        return this.pipeletParams;
    }

    public void setPipeletParams( List<PipeletParamPojo> pipeletParams ) {

        this.pipeletParams = pipeletParams;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {

        if ( !( obj instanceof PipeletPojo ) ) {
            return false;
        }

        if ( nxPipeletId == 0 ) {
            return super.equals( obj );
        }

        return nxPipeletId == ( (PipeletPojo) obj ).nxPipeletId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        if ( nxPipeletId == 0 ) {
            return super.hashCode();
        }

        return nxPipeletId;
    }

    public boolean isFrontend() {

        return frontend;
    }

    public void setFrontend( boolean frontend ) {

        this.frontend = frontend;
    }

    public boolean isEndpoint() {

        return endpoint;
    }

    public void setEndpoint( boolean endpoint ) {

        this.endpoint = endpoint;
    }

    public boolean isForward() {

        return forward;
    }

    public void setForward( boolean forward ) {

        this.forward = forward;
    }

}
