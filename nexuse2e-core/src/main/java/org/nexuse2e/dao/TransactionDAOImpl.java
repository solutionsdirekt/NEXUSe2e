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
package org.nexuse2e.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;
import org.nexuse2e.Constants;
import org.nexuse2e.NexusException;
import org.nexuse2e.controller.StateTransitionException;
import org.nexuse2e.logging.LogMessage;
import org.nexuse2e.pojo.ActionPojo;
import org.nexuse2e.pojo.ChoreographyPojo;
import org.nexuse2e.pojo.ConversationPojo;
import org.nexuse2e.pojo.LogPojo;
import org.nexuse2e.pojo.MessagePayloadPojo;
import org.nexuse2e.pojo.MessagePojo;
import org.nexuse2e.pojo.PartnerPojo;

/**
 * Data access object (DAO) to provide persistence services for transaction related entities.
 *
 * @author gesch
 */
public class TransactionDAOImpl extends BasicDAOImpl implements TransactionDAO {

    private static Logger              LOG           = Logger.getLogger( TransactionDAOImpl.class );

    private static Map<Integer, int[]> followUpConversationStates;
    private static Map<Integer, int[]> followUpMessageStates;

    static {
        followUpConversationStates = new HashMap<Integer, int[]>();
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_ERROR, new int[] {
            Constants.CONVERSATION_STATUS_IDLE, Constants.CONVERSATION_STATUS_COMPLETED,
            Constants.CONVERSATION_STATUS_PROCESSING} );
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_CREATED,
                new int[] { Constants.CONVERSATION_STATUS_PROCESSING} );
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_PROCESSING, new int[] {
            Constants.CONVERSATION_STATUS_AWAITING_ACK, Constants.CONVERSATION_STATUS_AWAITING_BACKEND,
            Constants.CONVERSATION_STATUS_SENDING_ACK, Constants.CONVERSATION_STATUS_ACK_SENT_AWAITING_BACKEND,
            Constants.CONVERSATION_STATUS_BACKEND_SENT_SENDING_ACK, Constants.CONVERSATION_STATUS_IDLE,
            Constants.CONVERSATION_STATUS_ERROR, Constants.CONVERSATION_STATUS_COMPLETED} );
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_AWAITING_ACK, new int[] {
            Constants.CONVERSATION_STATUS_COMPLETED, Constants.CONVERSATION_STATUS_ERROR,
            Constants.CONVERSATION_STATUS_IDLE} );
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_IDLE,
                new int[] { Constants.CONVERSATION_STATUS_PROCESSING} );
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_SENDING_ACK,
                new int[] { Constants.CONVERSATION_STATUS_ACK_SENT_AWAITING_BACKEND} );
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_ACK_SENT_AWAITING_BACKEND, new int[] {
            Constants.CONVERSATION_STATUS_COMPLETED, Constants.CONVERSATION_STATUS_ERROR,
            Constants.CONVERSATION_STATUS_IDLE} );
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_AWAITING_BACKEND,
                new int[] { Constants.CONVERSATION_STATUS_BACKEND_SENT_SENDING_ACK} );
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_BACKEND_SENT_SENDING_ACK, new int[] {
            Constants.CONVERSATION_STATUS_COMPLETED, Constants.CONVERSATION_STATUS_ERROR,
            Constants.CONVERSATION_STATUS_IDLE} );
        followUpConversationStates.put( Constants.CONVERSATION_STATUS_COMPLETED,
                new int[] { Constants.CONVERSATION_STATUS_ERROR} );

        followUpMessageStates = new HashMap<Integer, int[]>();
        followUpMessageStates.put( Constants.MESSAGE_STATUS_FAILED, new int[] { Constants.MESSAGE_STATUS_QUEUED,
            Constants.MESSAGE_STATUS_SENT} );
        followUpMessageStates.put( Constants.MESSAGE_STATUS_RETRYING, new int[] { Constants.MESSAGE_STATUS_FAILED,
            Constants.MESSAGE_STATUS_SENT} );
        followUpMessageStates.put( Constants.MESSAGE_STATUS_QUEUED, new int[] { Constants.MESSAGE_STATUS_RETRYING,
            Constants.MESSAGE_STATUS_FAILED, Constants.MESSAGE_STATUS_SENT} );
        followUpMessageStates.put( Constants.MESSAGE_STATUS_SENT, new int[] { Constants.MESSAGE_STATUS_QUEUED} );
    }

    private String getType( int messageType ) {
        switch (messageType) {
        case org.nexuse2e.messaging.Constants.INT_MESSAGE_TYPE_NORMAL:
            return "normal";
        case org.nexuse2e.messaging.Constants.INT_MESSAGE_TYPE_ACK:
            return "acknowledgement";
        case org.nexuse2e.messaging.Constants.INT_MESSAGE_TYPE_ERROR:
            return "error";
        }
        return "unknown";
    }
    /**
     * Helper method for trace log level output
     */
    private void printConversationInfo( String prefix, ConversationPojo pojo, MessagePojo lastMessage ) {
        if (pojo != null) {
            
            // DEBUG
            int id = pojo.getNxConversationId();
            pojo.setNxConversationId( 0 );
            String s = pojo.toString();
            pojo.setNxConversationId( id );
            // END OF DEBUG
            
            
            List<MessagePojo> messages = pojo.getMessages();
            if (lastMessage == null) {
                lastMessage = (messages == null || messages.isEmpty() ? null : messages.get( messages.size() - 1 ));
            }
            org.nexuse2e.configuration.EngineConfiguration cfg = org.nexuse2e.Engine.getInstance().getCurrentConfiguration();
            ActionPojo action = null;
            if (lastMessage != null) {
                try {
                    action = cfg.getActionFromChoreographyByNxActionId(
                            cfg.getChoreographyByNxChoreographyId( lastMessage.getConversation().getChoreography().getNxChoreographyId() ),
                            lastMessage.getAction().getNxActionId() );
                } catch (NexusException ex) {
                    ex.printStackTrace();
                }
            }
            LOG.trace(new LogMessage( prefix + s + " conversationId: " + pojo.getConversationId() + " " +
                    (messages == null ? 0 : messages.size()) + " messages" +
                    (lastMessage == null ? "" : ", last message type is " + getType( lastMessage.getType() ) +
                    ", messageId is " + lastMessage.getMessageId()) + (action == null ? "" : " (" + action.getName() + ")") +
                    ", thread " + Thread.currentThread().getName(),lastMessage) );
        } else if (lastMessage != null) {
            LOG.trace( new LogMessage(prefix + " message type is " + getType( lastMessage.getType() ) + ", messageId is " + lastMessage.getMessageId(),lastMessage) );
        }
    }
    //    public static final String TYPE_ACK      = "Acknowledgment";
    //    public static final String TYPE_DEFAULT  = "Normal";

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getConversationByConversationId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public ConversationPojo getConversationByConversationId( String conversationId ) {

        DetachedCriteria dc = DetachedCriteria.forClass( ConversationPojo.class );
        dc.add( Restrictions.eq( "conversationId", conversationId ) );
        List<ConversationPojo> result = (List<ConversationPojo>) getListThroughSessionFind( dc, 0, 0 );

        if ( result != null && result.size() > 0 ) {
            ConversationPojo pojo = result.get( 0 );
            if (LOG.isTraceEnabled()) {
                printConversationInfo( "loaded conversation:", pojo, null );
            }
            return pojo;
        }

        return null;
    } // getConversationByConversationId

    public ConversationPojo getConversationByConversationId( int nxConversationId ) {

        return (ConversationPojo) getHibernateTemplate().get( ConversationPojo.class, nxConversationId );
    } // getConversationByConversationId

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getMessageByMessageId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public MessagePojo getMessageByMessageId( String messageId ) throws NexusException {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
        dc.add( Restrictions.eq( "messageId", messageId ) );
        List<MessagePojo> result = (List<MessagePojo>) getListThroughSessionFind( dc, 0, 0 );

        if ( result != null && result.size() > 0 ) {
            return result.get( 0 );
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getMessageByReferencedMessageId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public MessagePojo getMessageByReferencedMessageId( String messageId ) throws NexusException {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
        dc.createCriteria( "referencedMessage" ).add( Restrictions.eq( "messageId", messageId ) );
        //StringBuffer query = new StringBuffer( "from MessagePojo where referencedMessage.messageId='" + messageId + "'" );

        List<MessagePojo> result = (List<MessagePojo>) getListThroughSessionFind( dc, 0, 0 );

        if ( result != null && result.size() > 0 ) {
            return result.get( 0 );
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getMessagesCount(java.lang.String, int, int, java.lang.String, java.lang.String, java.util.Date, java.util.Date)
     */
    public int getMessagesCount( String status, int nxChoreographyId, int nxPartnerId, String conversationId,
            String messageId, Date startDate, Date endDate ) throws NexusException {

        return getCountThroughSessionFind( getMessagesForReportCriteria( status, nxChoreographyId, nxPartnerId,
                conversationId, messageId, null, startDate, endDate, 0, false ) );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getActiveMessages()
     */
    @SuppressWarnings("unchecked")
    public List<MessagePojo> getActiveMessages() throws NexusException {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
        dc.add( Restrictions.in( "status", new Object[] { Constants.MESSAGE_STATUS_RETRYING,
                Constants.MESSAGE_STATUS_QUEUED} ) );
        dc.add( Restrictions.eq( "outbound", true ) );

        return (List<MessagePojo>) getListThroughSessionFind( dc, 0, 0 );
    } // getActiveMessages

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getMessagesForReport(java.lang.String, int, int, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date, int, int, int, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<MessagePojo> getMessagesForReport( String status, int nxChoreographyId, int nxPartnerId,
            String conversationId, String messageId, String type, Date start, Date end, int itemsPerPage, int page,
            int field, boolean ascending ) throws NexusException {

        DetachedCriteria dc = getMessagesForReportCriteria( status, nxChoreographyId, nxPartnerId, conversationId,
                messageId, type, start, end, field, ascending );

        return (List<MessagePojo>) getListThroughSessionFind( dc, itemsPerPage * page, itemsPerPage );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getConversationsForReport(java.lang.String, int, int, java.lang.String, java.util.Date, java.util.Date, int, int, int, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<ConversationPojo> getConversationsForReport( String status, int nxChoreographyId, int nxPartnerId,
            String conversationId, Date start, Date end, int itemsPerPage, int page, int field, boolean ascending )
            throws NexusException {

        return (List<ConversationPojo>) getListThroughSessionFind( getConversationsForReportCriteria( status,
                nxChoreographyId, nxPartnerId, conversationId, start, end, field, ascending ), itemsPerPage * page,
                itemsPerPage );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getConversationsCount(java.util.Date, java.util.Date)
     */
    public long getConversationsCount( Date start, Date end ) throws NexusException {

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        StringBuilder query = new StringBuilder( "select count(*) from nx_conversation conv " );
        Map<String, Timestamp> map = appendQueryDate( query, "conv", start, end );
        Query sqlquery = session.createSQLQuery( query.toString() );
        for (String name : map.keySet()) {
            sqlquery.setTimestamp( name, map.get( name ) );
        }
        return ( (Number) sqlquery.uniqueResult() ).longValue();

    }

    public long getMessagesCount( Date start, Date end ) throws NexusException {

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        StringBuilder query = new StringBuilder( "select count(*) from nx_message msg inner join nx_conversation conv on (msg.nx_conversation_id = conv.nx_conversation_id) " );
        Map<String, Timestamp> map = appendQueryDate( query, "conv", start, end );
        Query sqlquery = session.createSQLQuery( query.toString() );
        for (String name : map.keySet()) {
            sqlquery.setTimestamp( name, map.get( name ) );
        }
        return ( (Number) sqlquery.uniqueResult() ).longValue();

    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getLogCount(java.util.Date, java.util.Date)
     */
    public long getLogCount( Date start, Date end ) throws NexusException {

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        StringBuilder query = new StringBuilder( "select count(nx_log_id) from nx_log log " );
        Map<String, Timestamp> map = appendQueryDate( query, "log", start, end );
        Query sqlquery = session.createSQLQuery( query.toString() );
        for (String name : map.keySet()) {
            sqlquery.setTimestamp( name, map.get( name ) );
        }
        return ( (Number) sqlquery.uniqueResult() ).longValue();

    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getLogCount(java.util.Date, java.util.Date)
     */
    public Map<Level, Long> getLogCount( Date start, Date end, Level minLevel, Level maxLevel ) throws NexusException {

        int min = (minLevel == null ? 0 : minLevel.toInt());
        int max = (maxLevel == null ? 1000000 : maxLevel.toInt());
        
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        StringBuilder query = new StringBuilder( "select count(nx_log_id) as msg_count, severity from nx_log log " );
        if (minLevel != null || maxLevel != null) {
            query.append( "where severity >= " );
            query.append( min );
            query.append( " and severity <= " );
            query.append( max );
            query.append( " " );
        }
        Map<String, Timestamp> map = appendQueryDate( query, "log", start, end );
        query.append( " group by severity" );
        Query sqlquery = session.createSQLQuery( query.toString() );
        for (String name : map.keySet()) {
            sqlquery.setTimestamp( name, map.get( name ) );
        }
        List<?> l = sqlquery.list();
        Map<Level, Long> result = new LinkedHashMap<Level, Long>( l.size() );
        for (Object o : l) {
            Object[] kv = (Object[]) o;
            Long count = new Long( ((Number) kv[0]).longValue() );
            Level level = Level.toLevel( ((Number) kv[1]).intValue() );
            result.put( level, count );
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#removeLogEntries(java.util.Date, java.util.Date)
     */
    public long removeLogEntries( Date start, Date end ) throws NexusException {

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

        StringBuilder query = new StringBuilder( "delete from nx_log " );
        Map<String, Timestamp> map = appendQueryDate( query, "", start, end );
        Query sqlquery = session.createSQLQuery( query.toString() );
        for (String name : map.keySet()) {
            sqlquery.setTimestamp( name, map.get( name ) );
        }
        return sqlquery.executeUpdate();

    }

    /**
     * @param start
     * @param end
     * @return
     */
    private Map<String, Timestamp> appendQueryDate( StringBuilder queryString, String prefix, Date start, Date end ) {

        Timestamp startTs = (start == null ? null : (start instanceof Timestamp ? (Timestamp) start : new Timestamp( start.getTime() )));
        Timestamp endTs = (end == null ? null : (end instanceof Timestamp ? (Timestamp) end : new Timestamp( end.getTime() )));
        
        if (StringUtils.isEmpty( prefix )) {
            prefix = "";
        } else {
            prefix += ".";
        }
        boolean first = queryString.indexOf( "where" ) < 0;
        Map<String, Timestamp> map = new HashMap<String, Timestamp>( 2 );
        if ( start != null ) {
            if ( !first ) {
                queryString.append( " and " );
            } else {
                queryString.append( " where " );
            }
            queryString.append( prefix + "created_date >= :startDate" );
            map.put( "startDate", startTs );
            first = false;
        }
        if ( end != null ) {
            if ( !first ) {
                queryString.append( " and " );
            } else {
                queryString.append( " where " );
            }
            queryString.append( prefix + "created_date <= :endDate" );
            map.put( "endDate", endTs );
            first = false;
        }
        return map;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#removeConversations(java.util.Date, java.util.Date)
     */
    public long removeConversations( Date start, Date end ) throws NexusException {

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

        StringBuilder query = new StringBuilder( "delete from nx_message_label" );
        Map<String, Timestamp> map = null;
        if (start != null || end != null) {
            query.append( " where (select message.nx_message_id from nx_message message, nx_conversation conv where " +
            		"nx_message_label.nx_message_id = message.nx_message_id and message.nx_conversation_id = conv.nx_conversation_id" );
            map = appendQueryDate( query, "conv", start, end );
            query.append( ") is not null" );
        }
        LOG.debug( "sql1: " + query );
        Query sqlquery1 = session.createSQLQuery( query.toString() );
        if (map != null) {
            for (String name : map.keySet()) {
                sqlquery1.setTimestamp( name, map.get( name ) );
            }
        }

        map = null;
        query = new StringBuilder( "delete from nx_message_payload" );
        if (start != null || end != null) {
            query.append( " where (select message.nx_message_id from nx_message message, nx_conversation conv where " +
                    "nx_message_payload.nx_message_id = message.nx_message_id and message.nx_conversation_id = conv.nx_conversation_id" );
            map = appendQueryDate( query, "conv", start, end );
            query.append( ") is not null" );
        }

        LOG.debug( "sql2: " + query );
        Query sqlquery2 = session.createSQLQuery( query.toString() );
        if (map != null) {
            for (String name : map.keySet()) {
                sqlquery2.setTimestamp( name, map.get( name ) );
            }
        }
        
        map = null;
        query = new StringBuilder( "delete from nx_message" );
        if (start != null || end != null) {
            query.append( " where (select conv.nx_conversation_id from nx_conversation conv where nx_message.nx_conversation_id = conv.nx_conversation_id" );
            map = appendQueryDate( query, "conv", start, end );
            query.append( ") is not null" );
        }

        LOG.debug( "sql3: " + query );
        Query sqlquery3 = session.createSQLQuery( query.toString() );
        if (map != null) {
            for (String name : map.keySet()) {
                sqlquery3.setTimestamp( name, map.get( name ) );
            }
        }

        query = new StringBuilder( "delete from nx_conversation" );

        map = appendQueryDate( query, "nx_conversation", start, end );
        LOG.debug( "sql4: " + query );
        Query sqlquery4 = session.createSQLQuery( query.toString() );
        for (String name : map.keySet()) {
            sqlquery4.setTimestamp( name, map.get( name ) );
        }

        sqlquery1.executeUpdate();
        int result = sqlquery2.executeUpdate();
        sqlquery3.executeUpdate();
        sqlquery4.executeUpdate();

        return result;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getConversationsCount(java.lang.String, int, int, java.lang.String, java.util.Date, java.util.Date, int, boolean)
     */
    public int getConversationsCount( String status, int nxChoreographyId, int nxPartnerId, String conversationId,
            Date start, Date end, int field, boolean ascending ) throws NexusException {

        return getCountThroughSessionFind( getConversationsForReportCriteria( status, nxChoreographyId, nxPartnerId,
                conversationId, start, end, SORT_NONE, ascending ) );
    }

    /**
     * @param status
     * @param nxChoreographyId
     * @param nxPartnerId
     * @param conversationId
     * @param start
     * @param end
     * @param field
     * @param ascending
     * @return
     */
    private DetachedCriteria getConversationsForReportCriteria( String status, int nxChoreographyId, int nxPartnerId,
            String conversationId, Date start, Date end, int field, boolean ascending ) {

        DetachedCriteria dc = DetachedCriteria.forClass( ConversationPojo.class );
        
        if ( status != null ) {
            
            if ( status.indexOf( ',' ) == -1 ) {
                dc.add( Restrictions.eq( "status", Integer.parseInt( status ) ) );

            } else {
                String[] statusValues = status.split( "," );
                Integer[] intValues = new Integer[statusValues.length]; 
                for(int i = 0; i < statusValues.length; i ++){
                    intValues[i]=Integer.parseInt( statusValues[i] );
                }
                dc.add( Restrictions.in( "status", intValues ) );
            }
        }
        if ( nxChoreographyId != 0 ) {
            dc.createCriteria( "choreography" ).add( Restrictions.eq( "nxChoreographyId", nxChoreographyId ) );
        }
        if ( nxPartnerId != 0 ) {
            dc.createCriteria( "partner" ).add( Restrictions.eq( "nxPartnerId", nxPartnerId ) );
        }
        if ( conversationId != null ) {
            dc.add( Restrictions.like( "conversationId", "%" + conversationId.trim() + "%" ) );
        }
        if ( start != null ) {
            dc.add( Restrictions.ge( "createdDate", start ) );
        }
        if ( end != null ) {
            
            dc.add( Restrictions.le( "createdDate", end ) );
        }

        Order order = getSortOrder( field, ascending );
        if ( order != null ) {
            dc.addOrder( order );
        }

        return dc;
    }

    /**
     * @param status
     * @param nxChoreographyId
     * @param nxPartnerId
     * @param nxConversationId
     * @param messageId
     * @param type
     * @param start
     * @param end
     * @param field
     * @param ascending
     * @return
     */
    private DetachedCriteria getMessagesForReportCriteria( String status, int nxChoreographyId, int nxPartnerId,
            String conversationId, String messageId, String type, Date start, Date end, int field, boolean ascending ) {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );

        if ( status != null ) {
            if ( status.indexOf( ',' ) == -1 ) {
                dc.add( Restrictions.eq( "status", Integer.parseInt( status ) ) );

            } else {
                String[] statusValues = status.split( "," );
                Integer[] intValues = new Integer[statusValues.length]; 
                for(int i = 0; i < statusValues.length; i ++){
                    intValues[i]=Integer.parseInt( statusValues[i] );
                }
                dc.add( Restrictions.in( "status", intValues ) );
            }
        }
        
        DetachedCriteria conv = null;
        if ( conversationId != null ) {
            conv = dc.createCriteria( "conversation");
            conv.add(Restrictions.like( "conversationId", "%" + conversationId.trim() + "%" ) );
        }
        
        if ( nxChoreographyId != 0 ) {
            if(conv == null) {
                conv = dc.createCriteria( "conversation");
            }
            conv.createCriteria( "choreography" ).add(
                    Restrictions.eq( "nxChoreographyId", nxChoreographyId ) );
        }
        if ( nxPartnerId != 0 ) {
            if(conv == null) {
                conv = dc.createCriteria( "conversation");
            }
            conv.createCriteria( "partner" ).add(
                    Restrictions.eq( "nxPartnerId", nxPartnerId ) );
        }
        
        if ( messageId != null ) {
            dc.add( Restrictions.like( "messageId", "%" + messageId.trim() + "%" ) );
        }
        if ( type != null ) {
            dc.add( Restrictions.eq( "type", type ) );
        }
        if ( start != null ) {
            dc.add( Restrictions.ge( "createdDate", start ) );
        }
        if ( end != null ) {
            dc.add( Restrictions.le( "createdDate", end ) );
        }

        Order order = getSortOrder( field, ascending );
        if ( order != null ) {
            dc.addOrder( order );
        }
        return dc;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#storeTransaction(org.nexuse2e.pojo.ConversationPojo, org.nexuse2e.pojo.MessagePojo)
     */
    public void storeTransaction( ConversationPojo conversationPojo, MessagePojo messagePojo ) throws NexusException {

        if ( LOG.isTraceEnabled() ) {
	    	LOG.trace( new LogMessage( "(s)persisting state for message: "
	    			+ MessagePojo.getStatusName( messagePojo.getStatus() )
	    			+ "/" + ConversationPojo.getStatusName( messagePojo.getConversation().getStatus() ), messagePojo ) );
        }
        
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug( new LogMessage( "storeTransaction: " + conversationPojo + " - " + messagePojo, messagePojo ) );
        }

        saveOrUpdateRecord( conversationPojo );
        if ( LOG.isTraceEnabled() ) {
            printConversationInfo( "stored conversation:", conversationPojo, null );
        }

    } // storeTransaction

    /**
     * @param objectName
     * @param field
     * @param ascending
     * @return
     */
    private Order getSortOrder( int field, boolean ascending ) {

        Order order = null;

        switch ( field ) {
            case SORT_NONE:
                break;
            case SORT_CREATED:
                if ( ascending ) {
                    order = Order.asc( "createdDate" );
                } else {
                    order = Order.desc( "createdDate" );
                }
                break;
            case SORT_MODIFIED:
                if ( ascending ) {
                    order = Order.asc( "lastModifiedDate" );
                } else {
                    order = Order.desc( "lastModifiedDate" );
                }
                break;
            case SORT_STATUS:
                if ( ascending ) {
                    order = Order.asc( "status" );
                } else {
                    order = Order.desc( "status" );
                }
                break;
            case SORT_CPAID:
                if ( ascending ) {
                    order = Order.asc( "choreographyId" );
                } else {
                    order = Order.desc( "choreographyId" );
                }
                break;
            case SORT_ACTION:
                if ( ascending ) {
                    order = Order.asc( "action" );
                } else {
                    order = Order.desc( "action" );
                }
                break;
        }

        return order;
    } // getSortString

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getConversationsByPartner(org.nexuse2e.pojo.PartnerPojo)
     */
    @SuppressWarnings("unchecked")
    public List<ConversationPojo> getConversationsByPartner( PartnerPojo partner ) {

        //String query = "from ConversationPojo conv where conv.partner.nxPartnerId=" + partner.getNxPartnerId();

        DetachedCriteria dc = DetachedCriteria.forClass( ConversationPojo.class );
        dc.createCriteria( "partner" ).add( Restrictions.eq( "nxPartnerId", partner.getNxPartnerId() ) );

        return (List<ConversationPojo>) getListThroughSessionFind( dc, 0, 0 );

    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getConversationsByPartnerAndChoreography(org.nexuse2e.pojo.PartnerPojo, org.nexuse2e.pojo.ChoreographyPojo)
     */
    @SuppressWarnings("unchecked")
    public List<ConversationPojo> getConversationsByPartnerAndChoreography( PartnerPojo partner,
            ChoreographyPojo choreography ) throws NexusException {

        DetachedCriteria dc = DetachedCriteria.forClass( ConversationPojo.class );
        dc.createCriteria( "partner" ).add( Restrictions.eq( "nxPartnerId", partner.getNxPartnerId() ) )
                .createCriteria( "choreography" ).add(
                        Restrictions.eq( "nxChoreographyId", choreography.getNxChoreographyId() ) );

        return (List<ConversationPojo>) getListThroughSessionFind( dc, 0, 0 );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getConversationsByChoreography(org.nexuse2e.pojo.ChoreographyPojo)
     */
    @SuppressWarnings("unchecked")
    public List<ConversationPojo> getConversationsByChoreography( ChoreographyPojo choreography ) {

        DetachedCriteria dc = DetachedCriteria.forClass( ConversationPojo.class );
        dc.createCriteria( "choreography" ).add(
                Restrictions.eq( "nxChoreographyId", choreography.getNxChoreographyId() ) );

        return (List<ConversationPojo>) getListThroughSessionFind( dc, 0, 0 );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getMessagesByPartner(org.nexuse2e.pojo.PartnerPojo, int, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<MessagePojo> getMessagesByPartner( PartnerPojo partner, int field, boolean ascending )
            throws NexusException {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
        dc.createCriteria( "covnersation" ).createCriteria( "partner" ).add(
                Restrictions.eq( "nxPartnerId", partner.getNxPartnerId() ) );
        Order order = getSortOrder( field, ascending );
        if ( order != null ) {
            dc.addOrder( order );
        }

        return (List<MessagePojo>) getListThroughSessionFind( dc, 0, 0 );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#deleteMessage(org.nexuse2e.pojo.MessagePojo)
     */
    public void deleteMessage( MessagePojo messagePojo ) throws NexusException {

        LOG.debug( "deleteMessage: " + messagePojo );
        deleteRecord( messagePojo );
    } // updateMessage

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#deleteConversation(org.nexuse2e.pojo.ConversationPojo)
     */
    public void deleteConversation( ConversationPojo conversationPojo ) throws NexusException {

        LOG.debug( "deleteMessage: " + conversationPojo );
        deleteRecords( conversationPojo.getMessages() );
        deleteRecord( conversationPojo );
    } // updateMessage

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getMessagesByPartnerAndDirection(org.nexuse2e.pojo.PartnerPojo, boolean, int, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<MessagePojo> getMessagesByPartnerAndDirection( PartnerPojo partner, boolean outbound, int field,
            boolean ascending ) throws NexusException {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
        dc.createCriteria( "conversation" ).createCriteria( "partner" ).add(
                Restrictions.eq( "nxPartnerId", partner.getNxPartnerId() ) );
        dc.add( Restrictions.eq( "outbound", ( outbound ? 1 : 0 ) ) );

        Order order = getSortOrder( field, ascending );
        if ( order != null ) {
            dc.addOrder( order );
        }

        return (List<MessagePojo>) getListThroughSessionFind( dc, 0, 0 );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getMessagesByActionPartnerDirectionAndStatus(org.nexuse2e.pojo.ActionPojo, org.nexuse2e.pojo.PartnerPojo, boolean, int, int, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<MessagePojo> getMessagesByActionPartnerDirectionAndStatus( ActionPojo action, PartnerPojo partner,
            boolean outbound, int status, int field, boolean ascending ) {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );

        dc.createCriteria( "conversation" ).createCriteria( "partner" ).add(
                Restrictions.eq( "nxPartnerId", partner.getNxPartnerId() ) );
        dc.createCriteria( "action" ).add( Restrictions.eq( "name", action.getName() ) );
        dc.createCriteria( "action" ).createCriteria( "choreography" ).add(
                Restrictions.eq( "name", action.getChoreography().getName() ) );
        dc.add( Restrictions.eq( "outbound", ( outbound ? 1 : 0 ) ) );
        dc.createCriteria( "conversation" ).createCriteria( "partner" ).add(
                Restrictions.eq( "partnerId", partner.getPartnerId() ) );
        dc.add( Restrictions.eq( "status", status ) );

        Order order = getSortOrder( field, ascending );
        if ( order != null ) {
            dc.addOrder( order );
        }

        return (List<MessagePojo>) getListThroughSessionFind( dc, 0, 0 );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getMessagesByChoreographyAndPartner(org.nexuse2e.pojo.ChoreographyPojo, org.nexuse2e.pojo.PartnerPojo, int, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<MessagePojo> getMessagesByChoreographyAndPartner( ChoreographyPojo choreography, PartnerPojo partner,
            int field, boolean ascending ) {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
        dc.createCriteria( "conversation" ).createCriteria( "choreography" ).add(
                Restrictions.eq( "nxChoreographyId", choreography.getNxChoreographyId() ) );
        dc.createCriteria( "conversation" ).createCriteria( "partner" ).add(
                Restrictions.eq( "nxPartnerId", partner.getNxPartnerId() ) );
        Order order = getSortOrder( field, ascending );
        if ( order != null ) {
            dc.addOrder( order );
        }

        return (List<MessagePojo>) getListThroughSessionFind( dc, 0, 0 );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getMessagesByChoreographyPartnerAndConversation(org.nexuse2e.pojo.ChoreographyPojo, org.nexuse2e.pojo.PartnerPojo, org.nexuse2e.pojo.ConversationPojo, int, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<MessagePojo> getMessagesByChoreographyPartnerAndConversation( ChoreographyPojo choreography,
            PartnerPojo partner, ConversationPojo conversation, int field, boolean ascending ) {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );

        dc.createCriteria( "conversation" ).add(
                Restrictions.eq( "nxConversationId", conversation.getNxConversationId() ) );
        dc.createCriteria( "conversation" ).createCriteria( "choreography" ).add(
                Restrictions.eq( "nxChoreographyId", choreography.getNxChoreographyId() ) );
        dc.createCriteria( "conversation" ).createCriteria( "partner" ).add(
                Restrictions.eq( "nxPartnerId", partner.getNxPartnerId() ) );

        Order order = getSortOrder( field, ascending );
        if ( order != null ) {
            dc.addOrder( order );
        }

        return (List<MessagePojo>) getListThroughSessionFind( dc, 0, 0 );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#deleteLogEntry(org.nexuse2e.pojo.LogPojo)
     */
    public void deleteLogEntry( LogPojo logEntry ) throws NexusException {

        deleteRecord( logEntry );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#fetchLazyPayloads(org.nexuse2e.pojo.MessagePojo)
     */
    public List<MessagePayloadPojo> fetchLazyPayloads( MessagePojo message ) {

        lockRecord( message );
        List<MessagePayloadPojo> payloads = message.getMessagePayloads();
        // Force db access 
        payloads.size();
        return payloads;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#fetchLazyMessages(org.nexuse2e.pojo.ConversationPojo)
     */
    public List<MessagePojo> fetchLazyMessages( ConversationPojo conversation ) {

        lockRecord( conversation );
        List<MessagePojo> messages = conversation.getMessages();
        // Force db access 
        messages.size();
        return messages;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#updateTransaction(org.nexuse2e.pojo.MessagePojo, boolean)
     */
//    public void updateTransaction( MessagePojo message, boolean force ) throws NexusException, StateTransitionException {
//
//        
//        int messageStatus = message.getStatus();
//        int conversationStatus = message.getConversation().getStatus();
//        
//        if (messageStatus < Constants.MESSAGE_STATUS_FAILED
//                || messageStatus > Constants.MESSAGE_STATUS_STOPPED) {
//            throw new IllegalArgumentException( "Illegal message status: " + messageStatus
//                    + ", only values >= " + Constants.MESSAGE_STATUS_FAILED
//                    + " and <= " + Constants.MESSAGE_STATUS_STOPPED + " allowed" );
//        }
//        
//        if (conversationStatus < Constants.CONVERSATION_STATUS_ERROR
//                || conversationStatus > Constants.CONVERSATION_STATUS_COMPLETED) {
//            throw new IllegalArgumentException( "Illegal conversation status: " + conversationStatus
//                    + ", only values >= " + Constants.CONVERSATION_STATUS_ERROR
//                    + " and <= " + Constants.CONVERSATION_STATUS_COMPLETED + " allowed" );
//        }
//        
//        int allowedMessageStatus = messageStatus;
//        int allowedConversationStatus = conversationStatus;
//        
//        MessagePojo persistentMessage;
//        if ( message.getNxMessageId() > 0 ) {
//            persistentMessage = (MessagePojo) getRecordById( MessagePojo.class, message.getNxMessageId() );
//        } else {
//            persistentMessage = message;
//        }
//        if (persistentMessage != null) {
//            if (!force) {
//                allowedMessageStatus = getAllowedTransitionStatus( persistentMessage, messageStatus );
//                allowedConversationStatus = getAllowedTransitionStatus(
//                        persistentMessage.getConversation(), conversationStatus );
//            }
//            message.setStatus( allowedMessageStatus );
//            message.getConversation().setStatus( allowedConversationStatus );
//            
//            if (messageStatus == allowedMessageStatus && conversationStatus == allowedConversationStatus) {
//                boolean updateMessage = message.getNxMessageId() > 0;
//                
//                // persist unsaved messages first
//                List<MessagePojo> messages = message.getConversation().getMessages();
//                for (MessagePojo m : messages) {
//                    if (m.getNxMessageId() <= 0) {
//                        getHibernateTemplate().save( m );
//                    }
//                }
//
//                // we need to merge the message into the persistent message a persistent version exists
//                if (updateMessage) {
//                    getHibernateTemplate().merge( message );
//                }
//
//                // now, update the conversation status
//                getHibernateTemplate().merge( message.getConversation() );
//            }
//        }
//            
//          
//        
//        String errMsg = null;
//        
//        if (allowedMessageStatus != messageStatus) {
//            errMsg = "Illegal transition: Cannot set message status from " +
//            MessagePojo.getStatusName( allowedMessageStatus ) + " to " + MessagePojo.getStatusName( messageStatus );
//        }
//        if (allowedConversationStatus != conversationStatus) {
//            if (errMsg != null) {
//                errMsg += ", cannot set conversation status from " + ConversationPojo.getStatusName( allowedConversationStatus ) +
//                    " to " + ConversationPojo.getStatusName( conversationStatus );
//            } else {
//                errMsg = "Illegal transition: Cannot set conversation status from "
//                    + ConversationPojo.getStatusName( allowedConversationStatus ) + " to " + ConversationPojo.getStatusName( conversationStatus );
//            }
//        }
//        if (errMsg != null) {
//            throw new StateTransitionException( errMsg );
//        }
//        
//
//    } // updateTransaction

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#updateTransaction(org.nexuse2e.pojo.MessagePojo, boolean)
     */
    public void updateTransaction( MessagePojo message, boolean force ) throws NexusException, StateTransitionException {

        LOG.trace( new LogMessage("persisting state for message "+message.getConversation().getConversationId()+message.getMessageId()+
                ":"+MessagePojo.getStatusName( message.getStatus() )+"/"+ConversationPojo.getStatusName( message.getConversation().getStatus() ),message) );
        
        int messageStatus = message.getStatus();
        int conversationStatus = message.getConversation().getStatus();
        
        if (messageStatus < Constants.MESSAGE_STATUS_FAILED
                || messageStatus > Constants.MESSAGE_STATUS_STOPPED) {
            throw new IllegalArgumentException( "Illegal message status: " + messageStatus
                    + ", only values >= " + Constants.MESSAGE_STATUS_FAILED
                    + " and <= " + Constants.MESSAGE_STATUS_STOPPED + " allowed" );
        }
        
        if (conversationStatus < Constants.CONVERSATION_STATUS_ERROR
                || conversationStatus > Constants.CONVERSATION_STATUS_COMPLETED) {
            throw new IllegalArgumentException( "Illegal conversation status: " + conversationStatus
                    + ", only values >= " + Constants.CONVERSATION_STATUS_ERROR
                    + " and <= " + Constants.CONVERSATION_STATUS_COMPLETED + " allowed" );
        }
        
        int allowedMessageStatus = messageStatus;
        int allowedConversationStatus = conversationStatus;
        
        MessagePojo persistentMessage;
        ConversationPojo persistentConversation;
        if ( message.getNxMessageId() > 0 ) {
            persistentMessage = (MessagePojo) getHibernateTemplate().get( MessagePojo.class, message.getNxMessageId() );
            persistentConversation = (persistentMessage != null ? persistentMessage.getConversation() : null);
        } else {
            persistentMessage = message;
            if (message.getConversation() != null && message.getConversation().getNxConversationId() > 0) {
                persistentConversation = (ConversationPojo) getHibernateTemplate().get(
                        ConversationPojo.class, message.getConversation().getNxConversationId() );
            } else {
                persistentConversation = message.getConversation();
            }
        }
        if (persistentMessage != null) {
            if (!force) {
                allowedMessageStatus = getAllowedTransitionStatus( persistentMessage, messageStatus );
                allowedConversationStatus = getAllowedTransitionStatus(
                        persistentMessage.getConversation(), conversationStatus );
            }
            message.setStatus( allowedMessageStatus );
            message.getConversation().setStatus( allowedConversationStatus );
            
            if (messageStatus == allowedMessageStatus && conversationStatus == allowedConversationStatus) {
                if (persistentMessage != null) {
                    persistentMessage.setProperties( message );
                }
                if (persistentConversation != null) {
                    persistentConversation.setProperties( message.getConversation() );
                    persistentConversation.addMessage( message );
                    for (MessagePojo m : message.getConversation().getMessages()) {
                        if (m != null && m != message && m.getNxMessageId() == 0) {
                            persistentConversation.addMessage( m );
                        }
                    }
                    persistentConversation.setModifiedDate( new Date() );
                    getHibernateTemplate().saveOrUpdate( persistentConversation );
                }
            }
        }
            
          
        
        String errMsg = null;
        
        if (allowedMessageStatus != messageStatus) {
            errMsg = "Illegal transition: Cannot set message status from " +
            MessagePojo.getStatusName( allowedMessageStatus ) + " to " + MessagePojo.getStatusName( messageStatus );
        }
        if (allowedConversationStatus != conversationStatus) {
            if (errMsg != null) {
                errMsg += ", cannot set conversation status from " + ConversationPojo.getStatusName( allowedConversationStatus ) +
                    " to " + ConversationPojo.getStatusName( conversationStatus );
            } else {
                errMsg = "Illegal transition: Cannot set conversation status from "
                    + ConversationPojo.getStatusName( allowedConversationStatus ) + " to " + ConversationPojo.getStatusName( conversationStatus );
            }
        }
        if (errMsg != null) {
            throw new StateTransitionException( errMsg );
        }
        

    } // updateTransaction


    
    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getAllowedTransitionStatus(org.nexuse2e.pojo.ConversationPojo, int)
     */
    public int getAllowedTransitionStatus( ConversationPojo conversation, int conversationStatus ) {

        if ( conversation.getStatus() == conversationStatus ) {
            return conversationStatus;
        }
        int[] validStates = followUpConversationStates.get( conversation.getStatus() );
        if ( validStates != null ) {
            for ( int status : validStates ) {
                if ( status == conversationStatus ) {
                    return conversationStatus;
                }
            }
        }

        return conversation.getStatus();
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getAllowedTransitionStatus(org.nexuse2e.pojo.MessagePojo, int)
     */
    public int getAllowedTransitionStatus( MessagePojo message, int messageStatus ) {

        if ( message.getStatus() == messageStatus ) {
            return messageStatus;
        }
        int[] validStates = followUpMessageStates.get( message.getStatus() );
        if ( validStates != null ) {
            for ( int status : validStates ) {
                if ( status == messageStatus ) {
                    return messageStatus;
                }
            }
        }
        return message.getStatus();
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.dao.TransactionDAO#getCreatedMessagesSinceCount(java.util.Date)
     */
    public int getCreatedMessagesSinceCount( Date since ) throws NexusException {

        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
        dc.add( Restrictions.ge( "createdDate", since ) );
        return getCountThroughSessionFind( dc );
    }
    
    public List<int[]> getConversationStatesSince( Date since ) {
        
        DetachedCriteria dc = DetachedCriteria.forClass( ConversationPojo.class );
        dc.add( Restrictions.ge( "createdDate", since ) );
        ProjectionList pros = Projections.projectionList();
        pros.add( Projections.groupProperty( "status" ) );
        pros.add( Projections.count( "status" ) );
        dc.setProjection( pros );
        
        List<?> l = getListThroughSessionFind( dc, 0, Integer.MAX_VALUE );
        List<int[]> list = new ArrayList<int[]>( l.size() );
        for (Object o : l) {
            int[] kv = new int[] {
                    ((Number) ((Object[]) o)[0]).intValue(),
                    ((Number) ((Object[]) o)[1]).intValue()
            };
            list.add( kv );
        }
        
        return list;
    }
    
    public List<int[]> getMessageStatesSince( Date since ) {
        
        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
        dc.add( Restrictions.ge( "createdDate", since ) );
        dc.add( Restrictions.eq( "type", 1 ) );
        ProjectionList pros = Projections.projectionList();
        pros.add( Projections.groupProperty( "status" ) );
        pros.add( Projections.count( "status" ) );
        dc.setProjection( pros );
        
        List<?> l = getListThroughSessionFind( dc, 0, Integer.MAX_VALUE );
        List<int[]> list = new ArrayList<int[]>( l.size() );
        for (Object o : l) {
            int[] kv = new int[] {
                    ((Number) ((Object[]) o)[0]).intValue(),
                    ((Number) ((Object[]) o)[1]).intValue()
            };
            list.add( kv );
        }
        
        return list;
    }
    
    public List<String[]> getMessagesPerConversationSince( Date since ) {
        
        DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
        dc.createAlias( "conversation", "theConversation", Criteria.INNER_JOIN );
        dc.createAlias( "theConversation.choreography", "theChoreography", Criteria.INNER_JOIN );
        ProjectionList pros = Projections.projectionList();
        pros.add( Projections.groupProperty( "theChoreography.name" ) );
        pros.add( Projections.count( "messageId" ) );
        dc.setProjection( pros );
        dc.add( Restrictions.ge( "createdDate", since ) );
        dc.add( Restrictions.eq( "type", 1 ) );
        

        List<?> l = getListThroughSessionFind( dc, 0, Integer.MAX_VALUE );
        List<String[]> list = new ArrayList<String[]>( l.size() );
        for (Object o : l) {
            String[] kv = new String[] {
                    (((Object[]) o)[0]).toString(),
                    ((Number) ((Object[]) o)[1]).toString()
            };
            list.add( kv );
        }
        
        return list;
    }
    
    public List<int[]> getMessagesPerHourLast24Hours() {

        String hourFunction = "HOUR(created_date)";
        DatabaseType t = getDatabaseType();
        if (t == DatabaseType.MSSQL) {
            hourFunction = "DATEPART(hour, created_date)";
        } else if (t == DatabaseType.ORACLE) {
            hourFunction = "EXTRACT(HOUR FROM created_date)";
        }
        
        Calendar cal = Calendar.getInstance();
        
        cal.add( Calendar.DATE, -1 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.MILLISECOND, 0 );
        cal.add( Calendar.HOUR_OF_DAY, 1 );
        int currentHourOfDay = cal.get( Calendar.HOUR_OF_DAY );
        List<int[]> list = new ArrayList<int[]>( 24 );
        
        if (t == DatabaseType.DERBY) {
            // derby doesn't like functional expressions in GRUP BY, so we create one query per hour
            for (int i = 0; i < 24; i++) {
                DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
                dc.setProjection( Projections.count( "nxMessageId" ) );
                Date from = cal.getTime();
                int hourOfDay = cal.get( Calendar.HOUR_OF_DAY );
                cal.add( Calendar.HOUR_OF_DAY, 1 );
                Date to = cal.getTime();
                dc.add( Restrictions.eq( "type", 1 ) );
                dc.add( Restrictions.ge( "createdDate", from ) );
                dc.add( Restrictions.lt( "createdDate", to ) );
        
                List<?> l = getListThroughSessionFind( dc, 0, Integer.MAX_VALUE );
                Object o = l.get( 0 );
                int[] kv = new int[] {
                        hourOfDay,
                        ((Number) o).intValue()
                };
                list.add( kv );
            }
        } else {
            // default implementation for "normal" DBMS
            DetachedCriteria dc = DetachedCriteria.forClass( MessagePojo.class );
            ProjectionList pros = Projections.projectionList();
            pros.add( Projections.sqlGroupProjection(
                    hourFunction + " AS messageHour, COUNT(nx_message_id) AS messageCount", "messageHour",
                    new String[] { "messageHour", "messageCount" },
                    new Type[] { new IntegerType(), new IntegerType() } ) );
            dc.setProjection( pros );
            dc.add( Restrictions.ge( "createdDate", cal.getTime() ) );
            dc.add( Restrictions.eq( "type", 1 ) );
            dc.addOrder( Order.asc( "createdDate" ) );
    
            List<?> l = getListThroughSessionFind( dc, 0, Integer.MAX_VALUE );
    
            
            // create default 0-message entries
            for (int i = currentHourOfDay; i < currentHourOfDay + 24; i++) {
                list.add( new int[] { i % 24, 0 } );
            }
            
            // put list entries to appropriate positions
            for (Object o : l) {
                int hour = ((Number) ((Object[]) o)[0]).intValue();
                int value = ((Number) ((Object[]) o)[1]).intValue();
                
                int index = hour - currentHourOfDay;
                if (index < 0) {
                    index += 24;
                }
                list.set( index, new int[] { hour, value } );
            }
        }
        
        return list;
    }

    public Session getDBSession() {
        return getHibernateTemplate().getSessionFactory().openSession();
    }
    
    public void releaseDBSession( Session session ) {
        session.close();
    }
}