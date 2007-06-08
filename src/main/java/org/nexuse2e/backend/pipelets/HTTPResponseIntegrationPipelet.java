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

package org.nexuse2e.backend.pipelets;

import org.apache.log4j.Logger;
import org.nexuse2e.NexusException;
import org.nexuse2e.backend.pipelets.helper.RequestResponseData;
import org.nexuse2e.backend.pipelets.helper.ResponseSender;
import org.nexuse2e.configuration.EngineConfiguration;
import org.nexuse2e.configuration.ParameterDescriptor;
import org.nexuse2e.configuration.Constants.ParameterType;
import org.nexuse2e.messaging.MessageContext;

/**
 * @author mbreilmann
 *
 */
public class HTTPResponseIntegrationPipelet extends HTTPIntegrationPipelet {

    private static Logger      LOG               = Logger.getLogger( HTTPIntegrationPipelet.class );

    public static final String ACTION_PARAM_NAME = "action";
    public static final String DELAY_PARAM_NAME  = "delay";

    private int                delay             = 1000;
    private String             action            = null;

    public HTTPResponseIntegrationPipelet() {

        super();
        parameterMap.put( ACTION_PARAM_NAME, new ParameterDescriptor( ParameterType.STRING, "Action",
                "Action to trigger for outbound message.", "" ) );
        parameterMap.put( DELAY_PARAM_NAME, new ParameterDescriptor( ParameterType.STRING, "Delay",
                "Delay in milliseconds before outbound message is sent.", "1000" ) );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.messaging.AbstractPipelet#initialize(org.nexuse2e.configuration.EngineConfiguration)
     */
    @Override
    public void initialize( EngineConfiguration config ) {

        String actionValue = getParameter( ACTION_PARAM_NAME );
        if ( actionValue == null ) {
            LOG.error( "Parameter action has not been defined!" );
            return;
        } else {
            action = actionValue;
        }
        String delayString = getParameter( DELAY_PARAM_NAME );
        delay = 1000;
        if ( ( delayString != null ) && ( delayString.length() != 0 ) ) {
            delay = Integer.parseInt( delayString );
        }

        super.initialize( config );
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.messaging.Pipelet#processMessage(org.nexuse2e.messaging.MessageContext)
     */
    public MessageContext processMessage( MessageContext messageContext ) throws IllegalArgumentException,
            IllegalStateException, NexusException {

        RequestResponseData requestResponseData = null;

        if ( action == null ) {
            LOG.error( "Parameter action has not been defined!" );
            return messageContext;
        }

        // Execute the HTTP call first
        messageContext = super.processMessage( messageContext );

        if ( !( messageContext.getData() instanceof RequestResponseData ) ) {
            LOG.error( "Wrong class detected in data field, found " + messageContext.getData().getClass() );
            throw new NexusException( "Wrong class detected in data field, found "
                    + messageContext.getData().getClass() );
        }
        requestResponseData = (RequestResponseData) messageContext.getData();

        // Trigger new response message
        new Thread( new ResponseSender( messageContext.getChoreography().getName(), messageContext.getPartner()
                .getPartnerId(), messageContext.getConversation().getConversationId(), action, requestResponseData,
                delay ) ).start();

        LOG.debug( "Done!" );
        return messageContext;
    }

} // HTTPResponseIntegrationPipelet
