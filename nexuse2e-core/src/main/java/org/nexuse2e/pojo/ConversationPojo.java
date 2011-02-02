/**
 *  NEXUSe2e Business Messaging Open Source
 *  Copyright 2000-2009, Tamgroup and X-ioma GmbH
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation version 2.1 of
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

        return nxConversationId;
    }


    /**
     * Performs a flat copy of all properties (except for non-persistent properties,
     * and ID) from the given <code>ConversationPojo</code> onto this object.
     * @param conversation The conversation to be copied onto this conversation object.
     */
    public void setProperties( ConversationPojo conversation ) {
        if (conversation == null || conversation == this) {
            return;
        }

        this.conversationId = conversation.conversationId;
        this.partner = conversation.partner;
        this.choreography = conversation.choreography;
        this.currentAction = conversation.currentAction;
        this.createdDate = conversation.createdDate;
        this.endDate = conversation.endDate;
        this.modifiedDate = conversation.modifiedDate;
        this.modifiedNxUserId = conversation.modifiedNxUserId;
        this.status = conversation.status;
        this.messageCount = conversation.messageCount;
    }
    
    /**
     * Adds a <code>MessagePojo</code> to this <code>ConversationPojo</code>, checking if the
     * given message is already present on this <code>ConversationPojo</code> and merging
     * the given message if necessary.
     * @param message The message to be added.
     */
    public void addMessage( MessagePojo message ) {
        if (messages == null) {
            messages = new ArrayList<MessagePojo>();
        }
        boolean added = false;
        for (MessagePojo m : messages) {
            if (m == message) {
                added = true;
                break;
            }
            if (m.getNxMessageId() > 0) {
                if (m.getNxMessageId() == message.getNxMessageId()) {
                    m.setProperties( message );
                    added = true;
                    break;
                }
            } else {
                if (m.getMessageId() != null && m.getMessageId().equals(message.getMessageId())) {
                    m.setProperties( message );
                    added = true;
                    break;
                }
            }
        }
        if (!added) {
            messages.add( message );
        }
    }
    
    /**
     * @return
     * @deprecated This returns a persistent value that is never read or set by NEXUSe2e.
     * 			   The number does not reflect the actual number of messages in the conversation,
     * 			   but only the number that could have been set by a call of {@link #getMessageCount()}.
     * 			   Probably this method will be missing in future releases.
     */
    @Deprecated
    public int getMessageCount() {
    	return this.messageCount;
    }

    /**
     * @param messageCount
     * @deprecated This sets a value that will become persistent.
     * 			   The value will not be used by NEXUSe2e in any way, but returned by {@link #getMessageCount()}.
     * 			   Probably this method will be missing in future releases.
     */
    @Deprecated
    public void setMessageCount( int messageCount ) {
    
        this.messageCount = messageCount;
    }

    
    public static String getStatusName( int status ) {
        switch (status) {
        case Constants.CONVERSATION_STATUS_ERROR:
            return "ERROR";
        case Constants.CONVERSATION_STATUS_UNKNOWN:
            return "UNKNOWN";
        case Constants.CONVERSATION_STATUS_CREATED:
            return "CREATED";
        case Constants.CONVERSATION_STATUS_PROCESSING:
            return "PROCESSING";
        case Constants.CONVERSATION_STATUS_AWAITING_ACK:
            return "AWAITING_ACK";
        case Constants.CONVERSATION_STATUS_IDLE:
            return "IDLE";
        case Constants.CONVERSATION_STATUS_SENDING_ACK:
            return "SENDING_ACK";
        case Constants.CONVERSATION_STATUS_ACK_SENT_AWAITING_BACKEND:
            return "ACK_SENT_AWAITING_BACKEND";
        case Constants.CONVERSATION_STATUS_AWAITING_BACKEND:
            return "AWAITING_BACKEND";
        case Constants.CONVERSATION_STATUS_BACKEND_SENT_SENDING_ACK:
            return "BACKEND_SENT_SENDING_ACK";
        case Constants.CONVERSATION_STATUS_COMPLETED:
            return "COMPLETED";
        }
        return "UNKNOWN (" + status + ")";
    }
    
    /**
     * Gets the human-readable name of this message's type.
     * @return The type name, not <code>null</code>.
     */
    public String getStatusName() {
        return getStatusName(getStatus());
    }
    
    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "\n" );
		sb.append( "- - 8< - -" );
		sb.append( "\n" );
		sb.append( "State machine dump of conversation " + getConversationId() );
		sb.append( "\n" );
		sb.append( "Status: " + ConversationPojo.getStatusName( getStatus() ) );
		sb.append( "\n" );
		List<MessagePojo> messages = getMessages();
		if ( messages != null ) {
			sb.append( "Number of messages: " + messages.size() );
			sb.append( "\n" );
			if ( messages.size() > 0 ) {
				sb.append( "Messages: ");
				sb.append( "\n" );
				try {
    				for ( int i = 0; i < messages.size(); i++ ) {
    				    MessagePojo currMsg = messages.get( i );
    					sb.append( "\t#" + ( i + 1 ) + "\t" );
    					sb.append( currMsg.toString() );
    					sb.append( "\n" );
    				}
				} catch ( IndexOutOfBoundsException e ) {
				    sb.append( "List of messages possibly not complete, because of concurrent modification" );
                    sb.append( "\n" );
				}
			}
		} else {
			sb.append( "List of messages is null" );
			sb.append( "\n" );
		}
		sb.append( "- - >8 - -" );
		return sb.toString();
	}
}
