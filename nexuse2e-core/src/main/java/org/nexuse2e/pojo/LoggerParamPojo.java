/**
 *  NEXUSe2e Business Messaging Open Source
 *  Copyright 2000-2021, direkt gruppe GmbH
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation version 3 of
 *  the License.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.nexuse2e.pojo;

// Generated 29.11.2006 15:25:02 by Hibernate Tools 3.2.0.beta6a

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.nexuse2e.configuration.ParameterDescriptor;

/**
 * LoggerParamPojo generated by hbm2java
 */
@Entity
@Table(name = "nx_logger_param")
@XmlType(name = "LoggerParamType")
@XmlAccessorType(XmlAccessType.NONE)
public class LoggerParamPojo implements NEXUSe2ePojo {

    private static final long   serialVersionUID = 1L;

    // Fields    
    @Access(AccessType.PROPERTY)
    @Id
    @Column(name = "nx_logger_param_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int                 nxLoggerParamId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nx_logger_id", nullable = false)
    private LoggerPojo          logger;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date                createdDate;

    @Column(name = "modified_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date                modifiedDate;

    @Column(name = "modified_nx_user_id", nullable = false)
    private int                 modifiedNxUserId;

    @Column(name = "param_name", length = 64, nullable = false)
    private String              paramName;

    @Column(name = "param_label", length = 64)
    private String              label;

    @Column(name = "param_value", length = 1024, nullable = true)
    private String              value;

    @Column(name = "sequence_number")
    private int                 sequenceNumber;

    // non-persistent fields
    @Transient
    private ParameterDescriptor parameterDescriptor;

    // Constructors

    /** default constructor */
    public LoggerParamPojo() {
        createdDate = new Date();
        modifiedDate = createdDate;
    }

    /** minimal constructor */
    public LoggerParamPojo( LoggerPojo logger, Date createdDate, Date modifiedDate, int modifiedNxUserId,
            String paramName, String value ) {

        this.logger = logger;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.paramName = paramName;
        this.value = value;
    }

    /** full constructor */
    public LoggerParamPojo( LoggerPojo logger, Date createdDate, Date modifiedDate, int modifiedNxUserId,
            String paramName, String label, String value, int sequenceNumber ) {

        this.logger = logger;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.paramName = paramName;
        this.label = label;
        this.value = value;
        this.sequenceNumber = sequenceNumber;
    }

    // Property accessors
    public int getNxLoggerParamId() {

        return this.nxLoggerParamId;
    }

    public void setNxLoggerParamId( int nxLoggerParamId ) {

        this.nxLoggerParamId = nxLoggerParamId;
    }

    public int getNxId() {
        return nxLoggerParamId;
    }
    
    public void setNxId( int nxId ) {
        this.nxLoggerParamId = nxId;
    }
    
    public LoggerPojo getLogger() {

        return this.logger;
    }

    public void setLogger( LoggerPojo logger ) {

        this.logger = logger;
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
    public String getParamName() {

        return this.paramName;
    }

    public void setParamName( String paramName ) {

        this.paramName = paramName;
    }

    @XmlAttribute
    public String getLabel() {

        return this.label;
    }

    public void setLabel( String label ) {

        this.label = label;
    }

    @XmlAttribute
    public String getValue() {

        return this.value;
    }

    public void setValue( String value ) {

        this.value = value;
    }

    @XmlAttribute
    public int getSequenceNumber() {

        return this.sequenceNumber;
    }

    public void setSequenceNumber( int sequenceNumber ) {

        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the parameterDescriptor
     */
    public ParameterDescriptor getParameterDescriptor() {

        return parameterDescriptor;
    }

    /**
     * @param parameterDescriptor the parameterDescriptor to set
     */
    public void setParameterDescriptor( ParameterDescriptor parameterDescriptor ) {

        this.parameterDescriptor = parameterDescriptor;
    }
}
