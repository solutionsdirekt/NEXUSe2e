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
package org.nexuse2e.ui.action.communications;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.nexuse2e.NexusException;
import org.nexuse2e.configuration.CertificateType;
import org.nexuse2e.configuration.EngineConfiguration;
import org.nexuse2e.pojo.CertificatePojo;
import org.nexuse2e.ui.action.NexusE2EAction;
import org.nexuse2e.ui.form.CertificatePromotionForm;

/**
 * @author gesch
 * 
 */
public class StagingDeleteCertificateAction extends NexusE2EAction {

    private static String URL     = "staging.error.url";
    private static String TIMEOUT = "staging.error.timeout";

    /*
     * (non-Javadoc)
     * 
     * @see com.tamgroup.nexus.e2e.ui.action.NexusE2EAction#executeNexusE2EAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionMessages)
     */
    @Override
    public ActionForward executeNexusE2EAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response,
            EngineConfiguration engineConfiguration, ActionMessages errors, ActionMessages messages) throws Exception {

        ActionForward success = actionMapping.findForward(ACTION_FORWARD_SUCCESS);
        ActionForward error = actionMapping.findForward(ACTION_FORWARD_FAILURE);

        CertificatePromotionForm form = (CertificatePromotionForm) actionForm;

        int nxCertificateId = form.getNxCertificateId();

        if (nxCertificateId == 0) {
            ActionMessage errorMessage = new ActionMessage("generic.error", "missing SeqNo. No Certificate found to promote");
            errors.add(ActionMessages.GLOBAL_MESSAGE, errorMessage);
            addRedirect(request, URL, TIMEOUT);
            return error;
        }

        CertificatePojo cPojo = engineConfiguration.getCertificateByNxCertificateId(CertificateType.ALL.getOrdinal(), nxCertificateId);
        if (cPojo == null) {
            ActionMessage errorMessage = new ActionMessage("generic.error", "Certificate not found.");
            errors.add(ActionMessages.GLOBAL_MESSAGE, errorMessage);
            addRedirect(request, URL, TIMEOUT);
            return error;
        }
        try {
            engineConfiguration.deleteCertificate(cPojo);
        } catch (NexusException e1) {
            ActionMessage errorMessage = new ActionMessage("generic.error", e1.getMessage());
            errors.add(ActionMessages.GLOBAL_MESSAGE, errorMessage);
            addRedirect(request, URL, TIMEOUT);
            return error;
        }
        return success;
    }

}
