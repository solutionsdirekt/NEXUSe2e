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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.nexuse2e.configuration.CertificateType;
import org.nexuse2e.configuration.EngineConfiguration;
import org.nexuse2e.pojo.CertificatePojo;
import org.nexuse2e.ui.action.NexusE2EAction;
import org.nexuse2e.ui.form.ProtectedFileAccessForm;
import org.nexuse2e.util.EncryptionUtil;

/**
 * @author gesch
 * 
 */
public class CACertSavePWD extends NexusE2EAction {

    private static String URL     = "cacerts.error.url";
    private static String TIMEOUT = "cacerts.error.timeout";

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
        ProtectedFileAccessForm form = (ProtectedFileAccessForm) actionForm;

        try {

            List<CertificatePojo> certificates = engineConfiguration.getCertificates(CertificateType.CACERT_METADATA.getOrdinal(), null);

            String pwd = form.getPassword();
            String vpwd = form.getVerifyPwd();
            if (pwd.equals(vpwd)) {
                String encPwd = EncryptionUtil.encryptString(pwd);
                CertificatePojo certificate = null;
                if (certificates != null && certificates.size() > 0) {
                    certificate = certificates.get(0);
                } else {
                    certificate = new CertificatePojo();
                    certificate.setType(CertificateType.CACERT_METADATA.getOrdinal());
                    certificate.setCreatedDate(new Date());
                    certificate.setModifiedDate(new Date());
                    certificate.setName("CaKeyStoreData");
                }

                certificate.setPassword(encPwd);
                certificate.setBinaryData(new byte[0]);
                engineConfiguration.updateCertificate(certificate);
            }
        } catch (Exception e) {
            ActionMessage errorMessage = new ActionMessage("generic.error", e.getMessage());
            errors.add(ActionMessages.GLOBAL_MESSAGE, errorMessage);
            addRedirect(request, URL, TIMEOUT);
            e.printStackTrace();
            return error;
        }
        return success;
    }

}
