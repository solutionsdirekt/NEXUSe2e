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
package org.nexuse2e.ui.action;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.nexuse2e.configuration.EngineConfiguration;
import org.nexuse2e.configuration.GenericComparator;
import org.nexuse2e.pojo.ServicePojo;
import org.nexuse2e.ui.form.ServiceForm;

/**
 * This action provides data required to list all services (and their properties).
 * @author jonas.reese
 */
public class ServicesAction extends NexusE2EAction {

    /* (non-Javadoc)
     * @see org.nexuse2e.ui.action.NexusE2EAction#executeNexusE2EAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionMessages, org.apache.struts.action.ActionMessages)
     */
    @Override
    public ActionForward executeNexusE2EAction( ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, EngineConfiguration engineConfiguration, ActionMessages errors, ActionMessages messages )
            throws Exception {

        ActionForward actionForward = actionMapping.findForward( ACTION_FORWARD_SUCCESS );

        List<ServicePojo> services = engineConfiguration.getServices();
        List<ServiceForm> serviceList = new ArrayList<ServiceForm>();
        TreeSet<ServicePojo> sortedServices = new TreeSet<ServicePojo>( new GenericComparator<ServicePojo>( "name", true ) );
        sortedServices.addAll( services );
        for ( ServicePojo service : sortedServices ) {
            ServiceForm serviceForm = new ServiceForm();
            serviceForm.setProperties( service );
            serviceForm.setServiceInstance( engineConfiguration.getService(
                    service.getName() ) );
            serviceList.add( serviceForm );
        }
        request.setAttribute( ATTRIBUTE_COLLECTION, serviceList );

        if ( !errors.isEmpty() ) {
            actionForward = actionMapping.findForward( ACTION_FORWARD_FAILURE );
        }

        return actionForward;
    } // executeNexusE2EAction

}
