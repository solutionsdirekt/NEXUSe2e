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

// Generated 20.10.2006 15:50:01 by Hibernate Tools 3.2.0.beta6a

import java.util.Date;

/**
 * MessagePayloadPojo generated by hbm2java
 */
public class MessagePayloadPojo implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1828996888428444033L;

    // Fields    
    private int               nxMessagePayloadId;
    private MessagePojo       message;
    private int               sequenceNumber;
    private String            mimeType;
    private String            contentId;
    private byte[]            payloadData;
    private Date              createdDate;
    private Date              modifiedDate;
    private int               modifiedNxUserId;
    private String            charset;

    // Constructors

    /** default constructor */
    public MessagePayloadPojo() {

        createdDate = new Date();
        modifiedDate = createdDate;
    }
    
    public MessagePayloadPojo( MessagePojo message, int sequenceNumber, String mimeType, String charset, String contentId,
            byte[] payloadData, Date createdDate, Date modifiedDate, int modifiedNxUserId ) {
        
        this.message = message;
        this.sequenceNumber = sequenceNumber;
        this.mimeType = mimeType;
        this.contentId = contentId;
        this.payloadData = payloadData;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.charset = charset;
    }
    
    /** full constructor */
    public MessagePayloadPojo( MessagePojo message, int sequenceNumber, String mimeType, String contentId,
            byte[] payloadData, Date createdDate, Date modifiedDate, int modifiedNxUserId ) {

        this.message = message;
        this.sequenceNumber = sequenceNumber;
        this.mimeType = mimeType;
        this.contentId = contentId;
        this.payloadData = payloadData;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {

        MessagePayloadPojo clonedMessagePayloadPojo = new MessagePayloadPojo();

        clonedMessagePayloadPojo.setContentId( contentId );
        clonedMessagePayloadPojo.setCreatedDate( createdDate );
        clonedMessagePayloadPojo.setMessage( message );
        clonedMessagePayloadPojo.setMimeType( mimeType );
        clonedMessagePayloadPojo.setModifiedDate( modifiedDate );
        clonedMessagePayloadPojo.setModifiedNxUserId( modifiedNxUserId );
        clonedMessagePayloadPojo.setNxMessagePayloadId( nxMessagePayloadId );
        clonedMessagePayloadPojo.setPayloadData( payloadData.clone() );
        clonedMessagePayloadPojo.setSequenceNumber( sequenceNumber );
        clonedMessagePayloadPojo.setCharset( charset );
        
        return clonedMessagePayloadPojo;
    }

    // Property accessors
    public int getNxMessagePayloadId() {

        return this.nxMessagePayloadId;
    }

    public void setNxMessagePayloadId( int nxMessagePayloadId ) {

        this.nxMessagePayloadId = nxMessagePayloadId;
    }

    public MessagePojo getMessage() {

        return this.message;
    }

    public void setMessage( MessagePojo messagePojo ) {

        this.message = messagePojo;
    }

    public int getSequenceNumber() {

        return this.sequenceNumber;
    }

    public void setSequenceNumber( int sequenceNumber ) {

        this.sequenceNumber = sequenceNumber;
    }

    public String getMimeType() {

        return this.mimeType;
    }

    public void setMimeType( String mimeType ) {

        this.mimeType = mimeType;
    }

    public String getContentId() {

        return this.contentId;
    }

    public void setContentId( String contentId ) {

        this.contentId = contentId;
    }

    public byte[] getPayloadData() {

        return this.payloadData;
    }

    public void setPayloadData( byte[] payloadData ) {

        this.payloadData = payloadData;
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

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append( "nxMessagePayloadId: " + nxMessagePayloadId + "\n" );
        if ( message != null ) {
            buffer.append( "nxMessageId: " + message.getNxMessageId() + "\n" );
        } else {
            buffer.append( "nxMessageId: n/a\n" );
        }
        buffer.append( "sequenceNumber: " + sequenceNumber + "\n" );
        buffer.append( "mimeType: " + mimeType + "\n" );
        buffer.append( "contentId: " + contentId + "\n" );
        buffer.append( "payloadData: " + payloadData + "\n" );
        buffer.append( "createdDate: " + createdDate + "\n" );
        buffer.append( "modifiedDate: " + modifiedDate + "\n" );
        buffer.append( "modifiedNxUserId: " + modifiedNxUserId + "\n" );
        buffer.append( "charset: " + charset + "\n" );

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {

        if ( !( obj instanceof MessagePayloadPojo ) ) {
            return false;
        }

        if ( nxMessagePayloadId == 0 ) {
            return super.equals( obj );
        }
        // TODO Auto-generated method stub
        return nxMessagePayloadId == ( (MessagePayloadPojo) obj ).nxMessagePayloadId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        if ( nxMessagePayloadId == 0 ) {
            return super.hashCode();
        }

        // TODO Auto-generated method stub
        return nxMessagePayloadId;
    }

    
    /**
     * @return the charset
     */
    public String getCharset() {
    
        return charset;
    }

    
    /**
     * @param charset the charset to set
     */
    public void setCharset( String charset ) {
    
        this.charset = charset;
    }

}
