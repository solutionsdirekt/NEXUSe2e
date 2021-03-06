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
package org.nexuse2e.ui.action.pipelines;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.nexuse2e.Configurable;
import org.nexuse2e.configuration.ConfigurationUtil;
import org.nexuse2e.configuration.EngineConfiguration;
import org.nexuse2e.messaging.Pipelet;
import org.nexuse2e.pojo.ComponentPojo;
import org.nexuse2e.pojo.PipeletPojo;
import org.nexuse2e.pojo.PipelinePojo;
import org.nexuse2e.transport.TransportReceiver;
import org.nexuse2e.ui.action.NexusE2EAction;
import org.nexuse2e.ui.form.PipelineForm;

/**
 * @author gesch
 *
 */
public class PipelineUpdateAction extends NexusE2EAction {

    private static String URL     = "partner.error.url";
    private static String TIMEOUT = "partner.error.timeout";

    /* (non-Javadoc)
     * @see com.tamgroup.nexus.e2e.ui.action.NexusE2EAction#executeNexusE2EAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionMessages)
     */
    public ActionForward executeNexusE2EAction( ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, EngineConfiguration engineConfiguration, ActionMessages errors, ActionMessages messages )
            throws Exception {

        ActionForward success = actionMapping.findForward( ACTION_FORWARD_SUCCESS );
        ActionForward error = actionMapping.findForward( ACTION_FORWARD_FAILURE );

        PipelineForm form = (PipelineForm) actionForm;
        if ( form.isFrontend() ) {
            error = actionMapping.findForward( "frontendError" );
        } else {
            error = actionMapping.findForward( "backendError" );
        }

        String action = form.getSubmitaction();
        LOG.trace( "action: " + action );

        PipelinePojo pipeline = engineConfiguration.getPipelinePojoByNxPipelineId( form.getNxPipelineId() );

        if ( pipeline == null ) {
            ActionMessage errorMessage = new ActionMessage( "generic.error", "No pipeline found for id: "
                    + form.getNxPipelineId() );
            errors.add( ActionMessages.GLOBAL_MESSAGE, errorMessage );
            addRedirect( request, URL, TIMEOUT );
            return error;
        }

        boolean add = action.equals( "add" );
        boolean addReturn = action.equals( "addReturn" );
        if ( add || addReturn ) {
            ComponentPojo component = engineConfiguration.getComponentByNxComponentId( (add ? form.getActionNxId() : form.getActionNxIdReturn()) );
            if ( component != null ) {
                PipeletPojo pipelet = new PipeletPojo();
                pipelet.setComponent( component );
                pipelet.setCreatedDate( new Date() );
                pipelet.setModifiedDate( new Date() );
                pipelet.setName( component.getName() );
                pipelet.setForward( add );
                pipelet.setDescription( component.getDescription() );
                pipelet.setPipeline( pipeline );
                pipelet.setPosition( form.getPipelets().size() + 1 );

                try {
                    Object newComponent = Class.forName( component.getClassName() ).newInstance();
                    LOG.trace( "object:" + newComponent.getClass().getName() );
                    if ( ( newComponent instanceof Pipelet ) || ( newComponent instanceof TransportReceiver ) ) {
                        pipelet.getPipeletParams().addAll(
                                ConfigurationUtil.getConfiguration( (Configurable) newComponent, pipelet ) );
                    } else {
                        ActionMessage errorMessage = new ActionMessage( "generic.error",
                                "Referenced Component is no pipelet: " + component.getClassName() );
                        errors.add( ActionMessages.GLOBAL_MESSAGE, errorMessage );
                        addRedirect( request, URL, TIMEOUT );
                        return error;
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }

                form.getPipelets().add( pipelet );
                
                LOG.trace( "size: " + form.getPipelets().size() );
            }
            request.setAttribute( "keepData", "true" );
        }
        if ( action.equals( "delete" ) ) {
            int deletePosition = form.getSortaction();
            List<PipeletPojo> pipelets = form.getForwardPipelets();
            if ( pipelets != null && deletePosition >= 0 && deletePosition < pipelets.size() ) {
                form.getPipelets().remove( pipelets.get( deletePosition ) );
            }
            request.setAttribute( "keepData", "true" );
        }
        if ( action.equals( "deleteReturn" ) ) {
            int deletePosition = form.getSortaction();
            List<PipeletPojo> pipelets = form.getReturnPipelets();
            if ( pipelets != null && deletePosition >= 0 && deletePosition < pipelets.size() ) {
                form.getPipelets().remove( pipelets.get( deletePosition ) );
            }
            request.setAttribute( "keepData", "true" );
        }

        boolean sort = action.equals( "sort" );
        boolean sortReturn = action.equals( "sortReturn" );
        if ( sort || sortReturn ) {
            int direction = form.getSortingDirection();
            int sortaction = form.getSortaction();
            List<PipeletPojo> returnPipelets = form.getReturnPipelets();
            List<PipeletPojo> forwardPipelets = form.getForwardPipelets();
           
            List<PipeletPojo> pipelets = sort ? forwardPipelets : returnPipelets;

            LOG.trace( "direction: " + direction );
            LOG.trace( "sortaction: " + form.getSortaction() );

            if ( pipelets != null && pipelets.size() > 0 ) {
                // up
                if ( direction == 1 ) {
                    if (sortaction >= 1 && pipelets.size() > sortaction) {
                        PipeletPojo pipelet = pipelets.get( sortaction - 1 );
                        pipelets.set( sortaction - 1, pipelets.get( sortaction  ) );
                        pipelets.set( sortaction, pipelet );
                    }
                }
                // down
                else if ( direction == 2 ) {
                    if (pipelets.size() > sortaction + 1 && sortaction >= 0) {
                        PipeletPojo pipelet = pipelets.get( sortaction );
                        pipelets.set( sortaction, pipelets.get( sortaction + 1 ) );
                        pipelets.set( sortaction + 1, pipelet );
                    }
                }
                List<PipeletPojo> newPipeletList = new ArrayList<PipeletPojo>( forwardPipelets.size() + returnPipelets.size() );
                newPipeletList.addAll( forwardPipelets );
                newPipeletList.addAll( returnPipelets );
                for (int i = 0; i < newPipeletList.size(); i++) {
                    newPipeletList.get( i ).setPosition( i + 1 );
                }
                form.setPipelets( newPipeletList );
            }
            request.setAttribute( "keepData", "true" );

        }

        if (action.equals( "config" )) {
            return actionMapping.findForward( "config" );
        }
        
        if (action.equals( "configReturn" )) {
            return actionMapping.findForward( "configReturn" );
        }

        form.getProperties( pipeline, engineConfiguration );
        engineConfiguration.updatePipeline( pipeline );

        form.setSubmitaction( "" );

        return success;
    }

}
