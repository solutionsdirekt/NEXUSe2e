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
package org.nexuse2e.messaging.cidx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.nexuse2e.Engine;
import org.nexuse2e.NexusException;
import org.nexuse2e.configuration.EnumerationParameter;
import org.nexuse2e.configuration.ParameterDescriptor;
import org.nexuse2e.configuration.ParameterType;
import org.nexuse2e.messaging.AbstractPipelet;
import org.nexuse2e.messaging.Constants;
import org.nexuse2e.messaging.MessageContext;
import org.nexuse2e.pojo.MessagePayloadPojo;
import org.nexuse2e.pojo.MessagePojo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This pipelet extracts the routing information
 *
 * @author Jonas Reese
 * @version $LastChangedRevision:  $ - $LastChangedDate:  $ by $LastChangedBy:  $
 */
public class CidxRoutingPipelet extends AbstractPipelet {

    public static final String ACTION_MAP_PARAMETER_NAME = "actionMap";
    public static final String CONVERSATION_ID_XPATH_PARAMETER_NAME = "conversationIdXpath";
    
    
    public CidxRoutingPipelet() {
        setFrontendPipelet( true );
    }
    
    
    @Override
    public Map<String, ParameterDescriptor> getParameterMap() {
        parameterMap.put(
                CONVERSATION_ID_XPATH_PARAMETER_NAME,
                new ParameterDescriptor( ParameterType.STRING, "XPath statement to conversation ID", "XPath statement to the XML element that shall be used as conversation ID.", "" ) );
        parameterMap.put(
                ACTION_MAP_PARAMETER_NAME,
                new ParameterDescriptor( ParameterType.ENUMERATION, "", "Map CIDX document root to choreography/action. Example: name=\"OrderCreate\", value=\"CNOrder/OrderCreate\"", new EnumerationParameter() ) );
        return parameterMap;
    }

    @Override
    public MessageContext processMessage( MessageContext messageContext )
            throws IllegalArgumentException, IllegalStateException,
            NexusException {

        byte[] data = (byte[]) messageContext.getData();
        if (data == null) {
            throw new NexusException( "Payload must not be null" );
        }
        
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware( false );
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            XPath xPath = XPathFactory.newInstance().newXPath();
            Document document = builder.parse( new ByteArrayInputStream( data ) );
            
            Node root = document.getFirstChild();
            if (root == null) {
                throw new NexusException( "Received XML document does not contain a root element" );
            }
            String rootName = root.getNodeName();
            EnumerationParameter enumeration = getParameter( ACTION_MAP_PARAMETER_NAME );
            String choreographyAndAction = enumeration.getElements().get( rootName );
            if (choreographyAndAction == null) {
                throw new NexusException(
                        "Received XML document's root node '" + rootName
                        + "' is not mapped to a choreography and action" );
            }
            StringTokenizer st = new StringTokenizer( choreographyAndAction, "/" );
            String choreographyId = st.nextToken().trim();
            if (choreographyId.length() == 0) {
                throw new NexusException( "No choreography ID has been configured for XML root element '"
                        + rootName + "'" );
            }
            if (!st.hasMoreTokens()) {
                throw new NexusException( "No action ID has been configured for XML root element '"
                        + rootName + "', use syntax <choreographyID>/<actionId>" );
            }
            String actionId = st.nextToken().trim();
            if (actionId.length() == 0) {
                throw new NexusException( "An empty action ID has been configured for XML root element '"
                        + rootName + "" );
            }
            
            MessagePojo messagePojo = messageContext.getMessagePojo();

            String partnerId = xPath.evaluate( "Header/From/PartnerInformation/PartnerIdentifier", root );
            if (StringUtils.isEmpty( partnerId )) {
                throw new NexusException(
                        "Partner ID could not be extracted from CIDX document or empty PartnerIdentifier provided" );
            }
            String messageId = xPath.evaluate( "Header/ThisDocumentIdentifier/DocumentIdentifier", root );
            if (StringUtils.isEmpty( partnerId )) {
                throw new NexusException(
                        "Message ID could not be extracted from CIDX document or empty DocumentIdentifier provided" );
            }
            
            String conversationXPath = getParameter( CONVERSATION_ID_XPATH_PARAMETER_NAME );
            String conversationId = null;
            if (!StringUtils.isEmpty( conversationXPath )) {
                conversationId = xPath.evaluate( conversationXPath, document );
                if (StringUtils.isEmpty( conversationId )) {
                    conversationId = null;
                }
            }
            
            MessagePayloadPojo messagePayloadPojo = new MessagePayloadPojo();
            messagePayloadPojo.setMessage( messagePojo );
            messagePayloadPojo.setContentId( Engine.getInstance().getIdGenerator(
                    Constants.ID_GENERATOR_MESSAGE_PAYLOAD ).getId() );
            messagePayloadPojo.setMimeType( "text/xml" );
            List<MessagePayloadPojo> messagePayloads = new ArrayList<MessagePayloadPojo>( 1 );
            messagePayloads.add( messagePayloadPojo );
            messagePayloadPojo.setPayloadData( data );
            messagePojo.setMessagePayloads( messagePayloads );

            Engine.getInstance().getTransactionService().initializeMessage(
                    messagePojo,
                    messageId,
                    conversationId,
                    actionId,
                    partnerId,
                    choreographyId );

        } catch (ParserConfigurationException e) {
            throw new NexusException( e );
        } catch (SAXException e) {
            throw new NexusException( e );
        } catch (IOException e) {
            throw new NexusException( e );
        } catch (XPathExpressionException e) {
            throw new NexusException( e );
        }
        
        return messageContext;
    }
}
