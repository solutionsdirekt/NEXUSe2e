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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nexuse2e.Constants;

/**
 * ConversationPojo generated by hbm2java
 */
public class ConversationPojo implements NEXUSe2ePojo {
    
    /**
     * 
     */
    private static final long serialVersionUID = 75029204917804416L;

    // Fields    

    private int               nxConversationId;
    private ChoreographyPojo  choreography;
    private PartnerPojo       partner;
    private String            conversationId;
    private Date              createdDate;
    private Date              endDate;
    private Date              modifiedDate;
    private int               modifiedNxUserId;
    private int               status;
    private int               messageCount;
    private ActionPojo        currentAction;
    private List<MessagePojo> messages         = new ArrayList<MessagePojo>( 0 );

    // Constructors

    /** default constructor */
    public ConversationPojo() {

        status = Constants.CONVERSATION_STATUS_CREATED;
        createdDate = new Date();
        modifiedDate = createdDate;
    }

    /** minimal constructor */
    public ConversationPojo( ChoreographyPojo choreography, PartnerPojo partner, String conversationId,
            Date createdDate, Date modifiedDate, int modifiedNxUserId, int status, ActionPojo currentAction ) {

        this.choreography = choreography;
        this.partner = partner;
        this.conversationId = conversationId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.status = status;
        this.currentAction = currentAction;
    }

    /** full constructor */
    public ConversationPojo( ChoreographyPojo choreography, PartnerPojo partner, String conversationId,
            Date createdDate, Date endDate, Date modifiedDate, int modifiedNxUserId, int status,
            ActionPojo currentAction, List<MessagePojo> messages ) {

        this.choreography = choreography;
        this.partner = partner;
        this.conversationId = conversationId;
        this.createdDate = createdDate;
        this.endDate = endDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.status = status;
        this.currentAction = currentAction;
        this.messages = messages;
    }

    // Property accessors
    public int getNxConversationId() {

        return this.nxConversationId;
    }

    public void setNxConversationId( int nxConversationId ) {

        this.nxConversationId = nxConversationId;
    }

    public int getNxId() {
        return nxConversationId;
    }
    
    public void setNxId( int nxId ) {
        this.nxConversationId = nxId;
    }
    
    public ChoreographyPojo getChoreography() {

        return this.choreography;
    }

    public void setChoreography( ChoreographyPojo choreography ) {

        this.choreography = choreography;
    }

    public PartnerPojo getPartner() {

        return this.partner;
    }

    public void setPartner( PartnerPojo partner ) {

        this.partner = partner;
    }

    public String getConversationId() {

        return this.conversationId;
    }

    public void setConversationId( String conversationId ) {

        this.conversationId = conversationId;
    }

    public Date getCreatedDate() {

        return this.createdDate;
    }

    public void setCreatedDate( Date createdDate ) {

        this.createdDate = createdDate;
    }

    public Date getEndDate() {

        return this.endDate;
    }

    public void setEndDate( Date endDate ) {

        this.endDate = endDate;
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

    public int getStatus() {

        return this.status;
    }

    public void setStatus( int status ) {

        // LOG.trace( "Conversation ID: " + conversationId + " - status: " + status );
        this.status = status;
    }

    public ActionPojo getCurrentAction() {

        return this.currentAction;
    }

    public void setCurrentAction( ActionPojo currentAction ) {

        this.currentAction = currentAction;
    }

    public List<MessagePojo> getMessages() {

        return this.messages;
    }

    public void setMessages( List<MessagePojo> messages ) {

        this.messages = messages;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {

        if ( !( obj instanceof ConversationPojo ) ) {
            return false;
        }

        if ( nxConversationId == 0 ) {
            return super.equals( obj );
        }

        // TODO Auto-generated method stub
        return nxConversationId == ( (ConversationPojo) obj ).nxConversationId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        if ( nxConversationId == 0 ) {
            return super.hashCode();
        }

        // TODO Auto-generated method stub
        return nxConversationId;
    }

    
    public int getMessageCount() {
    
        return messageCount;
    }

    
    public void setMessageCount( int messageCount ) {
    
        this.messageCount = messageCount;
    }

}
