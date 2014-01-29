/**
 * NEXUSe2e Business Messaging Open Source
 * Copyright 2000-2009, Tamgroup and X-ioma GmbH
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

// Generated 27.11.2006 12:45:03 by Hibernate Tools 3.2.0.beta6a

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.nexuse2e.MessageStatus;
import org.nexuse2e.messaging.Constants;
import org.nexuse2e.messaging.ErrorDescriptor;

/**
 * MessagePojo generated by hbm2java
 */
@Entity
@Table(name = "nx_message")
@DynamicUpdate
public class MessagePojo implements NEXUSe2ePojo {

    /**
     * 
     */
    private static final long        serialVersionUID = 7535700207629664430L;

    // Fields

    @Id
    @Column(name = "nx_message_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int                      nxMessageId;

    @ManyToOne()
    @Index(name = "ix_message_1")
    @JoinColumn(name = "nx_conversation_id", nullable = false)
    private ConversationPojo         conversation;

    @Column(name = "message_id", length = 256, nullable = false)
    private String                   messageId;

    @Column(name = "header_data", length = 128000, nullable = true)
    @Lob
    private byte[]                   headerData;

    @Column(name = "type", nullable = false)
    private int                      type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Index(name = "ix_message_2")
    @JoinColumn(name = "nx_action_id", nullable = false)
    private ActionPojo               action;

    @Column(name = "status", nullable = false)
    private int                      status;

    @Column(name = "backend_status", nullable = false)
    private int                      backendStatus;

    @ManyToOne()
    @Index(name = "ix_message_3")
    @JoinColumn(name = "nx_trp_id")
    private TRPPojo                  TRP;

    @ManyToOne()
    @Index(name = "ix_message_4")
    @JoinColumn(name = "referenced_nx_message_id")
    private MessagePojo              referencedMessage;

    @Column(name = "retries", nullable = false)
    private int                      retries;

    @Column(name = "direction_flag", nullable = false)
    private boolean                  outbound;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date                     expirationDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date                     endDate;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date                     createdDate;

    @Column(name = "modified_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date                     modifiedDate;

    @Column(name = "modified_nx_user_id", nullable = false)
    private int                      modifiedNxUserId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "message")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MessagePayloadPojo> messagePayloads  = new ArrayList<MessagePayloadPojo>(0);

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "message")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MessageLabelPojo>   messageLabels    = new ArrayList<MessageLabelPojo>(0);

    @Transient
    private List<ErrorDescriptor>    errors           = new ArrayList<ErrorDescriptor>();

    @Transient
    private Map<String, String>      customParameters = new HashMap<String, String>();

    // Constructors

    /** default constructor */
    public MessagePojo() {

        createdDate = new Date();
        modifiedDate = createdDate;
    }

    /** minimal constructor */
    public MessagePojo(ConversationPojo conversation, String messageId, byte[] headerData, int type, ActionPojo action, int status, int retries,
            boolean outbound, Date createdDate, Date modifiedDate, int modifiedNxUserId) {

        this.conversation = conversation;
        this.messageId = messageId;
        this.headerData = headerData;
        this.type = type;
        this.action = action;
        this.status = status;
        this.retries = retries;
        this.outbound = outbound;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
    }

    /** full constructor */
    public MessagePojo(ConversationPojo conversation, String messageId, byte[] headerData, int type, ActionPojo action, int status, TRPPojo TRP,
            MessagePojo referencedMessageId, int retries, boolean outbound, Date expirationDate, Date endDate, Date createdDate, Date modifiedDate,
            int modifiedNxUserId, List<MessagePayloadPojo> messagePayloads, List<MessageLabelPojo> messageLabels) {

        this.conversation = conversation;
        this.messageId = messageId;
        this.headerData = headerData;
        this.type = type;
        this.action = action;
        this.status = status;
        this.TRP = TRP;
        this.referencedMessage = referencedMessageId;
        this.retries = retries;
        this.outbound = outbound;
        this.expirationDate = expirationDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.modifiedNxUserId = modifiedNxUserId;
        this.messagePayloads = messagePayloads;
        this.messageLabels = messageLabels;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {

        MessagePojo clonedMessagePojo = new MessagePojo();

        clonedMessagePojo.setAction(action);
        clonedMessagePojo.setConversation(conversation);
        clonedMessagePojo.setCreatedDate(createdDate);
        clonedMessagePojo.setCustomParameters(customParameters);
        clonedMessagePojo.setEndDate(endDate);
        clonedMessagePojo.setErrors(errors);
        clonedMessagePojo.setExpirationDate(expirationDate);
        clonedMessagePojo.setHeaderData(headerData != null ? headerData.clone() : null);
        clonedMessagePojo.setMessageId(messageId);
        List<MessageLabelPojo> clonedMessageLabels = new ArrayList<MessageLabelPojo>();
        for (MessageLabelPojo messageLabelPojo : clonedMessageLabels) {
            MessageLabelPojo clonedMessageLabelPojo = (MessageLabelPojo) messageLabelPojo.clone();
            clonedMessageLabelPojo.setMessage(clonedMessagePojo);
            clonedMessageLabels.add(clonedMessageLabelPojo);
        }
        clonedMessagePojo.setMessageLabels(clonedMessageLabels);
        List<MessagePayloadPojo> clonedMessagePayloads = new ArrayList<MessagePayloadPojo>();
        for (MessagePayloadPojo messagePayload : messagePayloads) {
            MessagePayloadPojo clonedMessagePayload = (MessagePayloadPojo) messagePayload.clone();
            clonedMessagePayload.setMessage(clonedMessagePojo);
            clonedMessagePayloads.add(clonedMessagePayload);
        }
        clonedMessagePojo.setMessagePayloads(clonedMessagePayloads);
        clonedMessagePojo.setModifiedDate(modifiedDate);
        clonedMessagePojo.setModifiedNxUserId(modifiedNxUserId);
        clonedMessagePojo.setOutbound(outbound);
        clonedMessagePojo.setReferencedMessage(referencedMessage);
        clonedMessagePojo.setRetries(retries);
        clonedMessagePojo.setStatus(status);
        clonedMessagePojo.setTRP(TRP);
        clonedMessagePojo.setType(type);

        return clonedMessagePojo;
    } // clone

    /**
     * Convenience method to retrieve the participant for this message.
     * 
     * @return The participant if it was found
     */
    public ParticipantPojo getParticipant() {

        if (conversation != null) {
            ChoreographyPojo choreographyPojo = conversation.getChoreography();
            PartnerPojo partnerPojo = conversation.getPartner();
            if ((choreographyPojo != null) && (partnerPojo != null)) {
                List<ParticipantPojo> participants = choreographyPojo.getParticipants();
                if (participants != null) {
                    for (ParticipantPojo pojo : participants) {
                        if ((pojo.getPartner() != null) && (pojo.getPartner().getNxPartnerId() == partnerPojo.getNxPartnerId())) {
                            return pojo;
                        }
                    }
                }
            }
        }

        return null;
    }

    // Property accessors
    public int getNxMessageId() {

        return this.nxMessageId;
    }

    public void setNxMessageId(int nxMessageId) {

        this.nxMessageId = nxMessageId;
    }

    public int getNxId() {
        return nxMessageId;
    }

    public void setNxId(int nxId) {
        this.nxMessageId = nxId;
    }

    public ConversationPojo getConversation() {

        return this.conversation;
    }

    public void setConversation(ConversationPojo conversation) {

        this.conversation = conversation;
    }

    public String getMessageId() {

        return this.messageId;
    }

    public void setMessageId(String messageId) {

        this.messageId = messageId;
    }

    public byte[] getHeaderData() {

        return this.headerData;
    }

    public void setHeaderData(byte[] headerData) {

        this.headerData = headerData;
    }

    public int getType() {

        return this.type;
    }

    public void setType(int type) {

        this.type = type;
    }

    public ActionPojo getAction() {

        return this.action;
    }

    public void setAction(ActionPojo action) {

        this.action = action;
    }

    public int getStatus() {

        return this.status;
    }

    public void setStatus(int status) {

        // LOG.trace( "Message ID: " + messageId + " - status: " + status );

        this.status = status;
    }

    public int getBackendStatus() {
        return backendStatus;
    }

    public void setBackendStatus(int backendStatus) {
        this.backendStatus = backendStatus;
    }

    public TRPPojo getTRP() {

        return this.TRP;
    }

    public void setTRP(TRPPojo TRP) {

        this.TRP = TRP;
    }

    public MessagePojo getReferencedMessage() {

        return this.referencedMessage;
    }

    public void setReferencedMessage(MessagePojo referencedMessageId) {

        this.referencedMessage = referencedMessageId;
    }

    public int getRetries() {

        return this.retries;
    }

    public void setRetries(int retries) {

        this.retries = retries;
    }

    public boolean isOutbound() {

        return this.outbound;
    }

    public void setOutbound(boolean outbound) {

        this.outbound = outbound;
    }

    public Date getExpirationDate() {

        return this.expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {

        this.expirationDate = expirationDate;
    }

    public Date getEndDate() {

        return this.endDate;
    }

    public void setEndDate(Date endDate) {

        this.endDate = endDate;
    }

    public Date getCreatedDate() {

        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {

        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {

        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {

        this.modifiedDate = modifiedDate;
    }

    public int getModifiedNxUserId() {

        return this.modifiedNxUserId;
    }

    public void setModifiedNxUserId(int modifiedNxUserId) {

        this.modifiedNxUserId = modifiedNxUserId;
    }

    public List<MessagePayloadPojo> getMessagePayloads() {

        return this.messagePayloads;
    }

    public void setMessagePayloads(List<MessagePayloadPojo> messagePayloads) {

        this.messagePayloads = messagePayloads;
    }

    public List<MessageLabelPojo> getMessageLabels() {

        return this.messageLabels;
    }

    public void setMessageLabels(List<MessageLabelPojo> messageLabels) {

        this.messageLabels = messageLabels;
    }

    /**
     * @param key
     * @param value
     */
    public void addCustomParameter(String key, String value) {
        if (customParameters == null) {
            customParameters = new HashMap<String, String>();
        }
        customParameters.put(key, value);
    }

    /**
     * @return the customParameters
     */
    public Map<String, String> getCustomParameters() {

        return customParameters;
    }

    /**
     * @param customParameters
     *            the customParameters to set
     */
    public void setCustomParameters(Map<String, String> customParameters) {

        this.customParameters = customParameters;
    }

    /**
     * @return the errors
     */
    public List<ErrorDescriptor> getErrors() {

        return errors;
    }

    /**
     * @param errors
     *            the errors to set
     */
    public void setErrors(List<ErrorDescriptor> errors) {

        this.errors = errors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof MessagePojo)) {
            return false;
        }
        if (nxMessageId == 0) {
            return super.equals(obj);
        }

        return nxMessageId == ((MessagePojo) obj).nxMessageId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        if (nxMessageId == 0) {
            return super.hashCode();
        }

        return nxMessageId;
    }

    /**
     * Performs a flat copy of all properties (except for non-persistent properties, object references
     * and ID) from the given <code>MessagePojo</code> onto this object.
     * 
     * @param message
     *            The message to be copied onto this message object.
     */
    public void setProperties(MessagePojo message) {
        if (message == null || message == this) {
            return;
        }

        this.messageId = message.messageId;
        this.headerData = message.headerData;
        this.type = message.type;
        this.status = message.status;
        this.retries = message.retries;
        this.outbound = message.outbound;
        this.expirationDate = message.expirationDate;
        this.endDate = message.endDate;
        this.createdDate = message.createdDate;
        this.modifiedDate = message.modifiedDate;
        this.modifiedNxUserId = message.modifiedNxUserId;
    }

    public static String getStatusName(int status) {
        try {
            return MessageStatus.getByOrdinal(status).toString();
        } catch (Exception e) {
            // Pokemon!
            return "UNKNOWN";
        }
    }

    /**
     * Gets the human-readable name of this message's type.
     * 
     * @return The type name, not <code>null</code>.
     */
    public String getStatusName() {
        return getStatusName(getStatus());
    }

    public static String getBackendStatusName(int backendStatus) {
        try {
            return MessageStatus.getByOrdinal(backendStatus).toString();
        } catch (Exception e) {
            // Pokemon!
            return "UNKNOWN";
        }
    }

    /**
     * Gets the human-readable name of this message's type.
     * 
     * @return The type name, not <code>null</code>.
     */
    public String getBackendStatusName() {
        return getStatusName(getBackendStatus());
    }

    public static String getTypeName(int type) {
        switch (type) {
            case org.nexuse2e.messaging.Constants.INT_MESSAGE_TYPE_ACK:
                return "Acknowledgement";
            case org.nexuse2e.messaging.Constants.INT_MESSAGE_TYPE_NORMAL:
                return "Normal";
            case org.nexuse2e.messaging.Constants.INT_MESSAGE_TYPE_ERROR:
                return "Error";
            default:
                return "Unknown message type (" + type + ")";
        }
    }

    /**
     * Gets the human-readable name of this message's type.
     * 
     * @return The type name, not <code>null</code>.
     */
    public String getTypeName() {
        return getTypeName(getType());
    }

    /**
     * Returns <code>true</code> if and only if this message is of type {@link Constants.INT_MESSAGE_TYPE_ACK}.
     * 
     * @return <code>true</code> if and only if this is an ack message.
     */
    public boolean isAck() {
        return (getType() == Constants.INT_MESSAGE_TYPE_ACK);
    }

    /**
     * Returns <code>true</code> if and only if this message is of type {@link Constants.INT_MESSAGE_TYPE_NORMAL}.
     * 
     * @return <code>true</code> if and only if this is a normal message.
     */
    public boolean isNormal() {
        return (getType() == Constants.INT_MESSAGE_TYPE_NORMAL);
    }

    /**
     * Returns <code>true</code> if and only if this message is of type {@link Constants.INT_MESSAGE_TYPE_ERROR}.
     * 
     * @return <code>true</code> if and only if this is an error message.
     */
    public boolean isError() {
        return (getType() == Constants.INT_MESSAGE_TYPE_ERROR);
    }

    @Override
    public String toString() {
        String actionName = "<not loaded>";
        if (action != null) {
            try {
                actionName = action.getName();
            } catch (Exception notLoadedException) {
                // avoid LazyInitializationException being thrown here
            }
        }
        return messageId + " " + getTypeName() + " " + actionName + " " + getStatusName(status) + " " + createdDate;
    }
}
