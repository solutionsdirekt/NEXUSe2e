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
package org.nexuse2e.ui.action.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.nexuse2e.configuration.ConfigurationAccessService;
import org.nexuse2e.configuration.EngineConfiguration;
import org.nexuse2e.pojo.RolePojo;
import org.nexuse2e.pojo.UserPojo;
import org.nexuse2e.ui.action.NexusE2EAction;
import org.nexuse2e.ui.form.RoleForm;


/**
 * @author Sebastian Schulze
 * @date 29.01.2007
 */
public class RoleDeleteAction extends NexusE2EAction {

    /* (non-Javadoc)
     * @see org.nexuse2e.ui.action.NexusE2EAction#executeNexusE2EAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionMessages, org.apache.struts.action.ActionMessages)
     */
    @Override
    public ActionForward executeNexusE2EAction( ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, EngineConfiguration engineConfiguration, ActionMessages errors, ActionMessages messages )
            throws Exception {

        int nxRoleId = ( (RoleForm) actionForm ).getNxRoleId();
        ConfigurationAccessService cas = engineConfiguration;
        RolePojo role = cas.getRoleByNxRoleId( nxRoleId );
        if ( role != null ) {
            // ensure that role is not referenced by any user
            List<UserPojo> users = cas.getUsers( null );
            StringBuffer sb = new StringBuffer();
            // ensure that user list will be not modified
            synchronized ( users ) {
                for ( UserPojo user : users ) {
                    if ( user.getRole() != null && user.getRole().getNxRoleId() == role.getNxRoleId() ) {
                        sb.append( "<li>" +  user.getLastName() + ", " + user.getFirstName() + ( user.getMiddleName() != null ? " " + user.getMiddleName() : "" ) + "</li>" );
                    }
                }
            }
            if ( sb.length() > 0 ) {
                errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "nexususer.error.role.referenced", "<ul>" + sb.toString() + "</ul>" ) );
            }
        } else {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "nexususer.error.role.notFound" ) );
        }
        
        ActionForward forward = null;
        if ( errors.size() == 0 ) {
            // delete persistent role
            cas.deleteRole( role );
            forward = actionMapping.findForward( ACTION_FORWARD_SUCCESS );
        }
        else {
            forward = actionMapping.findForward( ACTION_FORWARD_FAILURE );
        }
        return  forward;
    }

}
