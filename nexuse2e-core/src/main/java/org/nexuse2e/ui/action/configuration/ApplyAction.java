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
package org.nexuse2e.ui.action.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.nexuse2e.Engine;
import org.nexuse2e.configuration.EngineConfiguration;
import org.nexuse2e.pojo.UserPojo;
import org.nexuse2e.ui.action.NexusE2EAction;

/**
 * Applies the pending configuration for the current user. Does nothing if the configuration
 * did not change.
 *
 * @author Jonas Reese
 * @version $LastChangedRevision:  $ - $LastChangedDate:  $ by $LastChangedBy:  $
 */
public class ApplyAction extends NexusE2EAction {

    @Override
    public ActionForward executeNexusE2EAction(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response,
            EngineConfiguration engineConfiguration, ActionMessages errors,
            ActionMessages messages) throws Exception {
        
        if (engineConfiguration.isChanged()) {
            
            UserPojo user = (UserPojo) request.getSession().getAttribute( ATTRIBUTE_USER );
            
            if (user != null) {
                Engine.getInstance().invalidateConfiguration( user.getNxUserId() );
                Engine.getInstance().setCurrentConfiguration( engineConfiguration );
                request.setAttribute(
                        ATTRIBUTE_CONFIGURATION, Engine.getInstance().getConfiguration( user.getNxUserId() ) );
            }
        }
        
        return actionMapping.findForward( ACTION_FORWARD_SUCCESS );
    }

}
