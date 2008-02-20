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

// Generated 29.11.2006 16:06:23 by Hibernate Tools 3.2.0.beta6a

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Level;

/**
 * LoggerPojo generated by hbm2java
 */
@XmlType(name = "LoggerType")
@XmlAccessorType(XmlAccessType.NONE)
public class LoggerPojo implements NEXUSe2ePojo {

    // Fields    

    /**
     * 
     */
    private static final long           serialVersionUID = -4468546723751324561L;
    private int                         nxLoggerId;
    private ComponentPojo               component;
    private Date                        createdDate;
    private Date                        modifiedDate;
    private int                         modifiedNxUserId;
    private int                         threshold        = Level.INFO_INT;
    private String                      name;
    private boolean                     autostart;
    private String                      filter;
    private String                      description;
    @XmlElementWrapper(name = "LoggerParams")
    @XmlElement(name = "LoggerParam")
    private Collection<LoggerParamPojo> loggerParams;

    // non persistent fields

    private boolean                     running;

    private int                         nxComponentId;

    // Constructors

    /** default constructor */
    public LoggerPojo() {

        createdDate = new Date();
        modifiedDate = createdDate;
    }

    /** minimal constructor */
    public LoggerPojo( ComponentPojo component, Date createdDate, Date modifiedDate, int modifiedNxUserId, String name,
            boolean autostart, String filter ) {

        this.component = component;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.name = name;
        this.autostart = autostart;
        this.filter = filter;
    }

    /** full constructor */
    public LoggerPojo( ComponentPojo component, Date createdDate, Date modifiedDate, int modifiedNxUserId, String name,
            boolean autostart, String filter, String description, Collection<LoggerParamPojo> loggerParams ) {

        this.component = component;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.name = name;
        this.autostart = autostart;
        this.filter = filter;
        this.description = description;
        this.loggerParams = loggerParams;
    }

    // Property accessors
    @XmlAttribute
    public int getNxLoggerId() {

        return this.nxLoggerId;
    }

    public int getNxId() {
        return nxLoggerId;
    }
    
    public void setNxId( int nxId ) {
        this.nxLoggerId = nxId;
    }
    
    public void setNxLoggerId( int nxLoggerId ) {

        this.nxLoggerId = nxLoggerId;
    }

    public ComponentPojo getComponent() {

        return this.component;
    }

    public void setComponent( ComponentPojo component ) {

        this.component = component;
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
    public String getName() {

        return this.name;
    }

    public void setName( String name ) {

        this.name = name;
    }

    @XmlAttribute
    public boolean isAutostart() {

        return this.autostart;
    }

    public void setAutostart( boolean autostart ) {

        this.autostart = autostart;
    }

    @XmlAttribute
    public String getFilter() {

        return this.filter;
    }

    public void setFilter( String filter ) {

        this.filter = filter;
    }

    @XmlAttribute
    public String getDescription() {

        return this.description;
    }

    public void setDescription( String description ) {

        this.description = description;
    }

    public Collection<LoggerParamPojo> getLoggerParams() {

        if ( this.loggerParams == null ) {
            this.loggerParams = new Vector<LoggerParamPojo>();
        }
        return this.loggerParams;
    }

    public void setLoggerParams( Collection<LoggerParamPojo> loggerParams ) {

        this.loggerParams = loggerParams;
    }

    /**
     * @return the running
     */
    public boolean isRunning() {

        return running;
    }

    /**
     * @param running the running to set
     */
    public void setRunning( boolean running ) {

        this.running = running;
    }

    /**
     * @return the threshold
     */
    @XmlAttribute
    public int getThreshold() {

        return threshold;
    }

    /**
     * @param threshold the threshold to set
     */
    public void setThreshold( int threshold ) {

        this.threshold = threshold;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {

        if ( !( obj instanceof LoggerPojo ) ) {
            return false;
        }

        if ( nxLoggerId == 0 ) {
            return super.equals( obj );
        }

        return nxLoggerId == ( (LoggerPojo) obj ).nxLoggerId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        if ( nxLoggerId == 0 ) {
            return super.hashCode();
        }

        return nxLoggerId;
    }

}
