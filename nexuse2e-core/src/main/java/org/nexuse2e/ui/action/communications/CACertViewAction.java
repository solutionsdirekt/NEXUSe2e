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

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.nexuse2e.configuration.CertificateType;
import org.nexuse2e.configuration.EngineConfiguration;
import org.nexuse2e.pojo.CertificatePojo;
import org.nexuse2e.ui.action.NexusE2EAction;
import org.nexuse2e.ui.form.CertificatePropertiesForm;
import org.nexuse2e.util.CertificateUtil;

/**
 * @author gesch
 * 
 */
public class CACertViewAction extends NexusE2EAction {

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
        CertificatePropertiesForm form = (CertificatePropertiesForm) actionForm;

        Integer nxCertificateId = null;
        try{
            nxCertificateId = Integer.valueOf(request.getParameter("nxCertificateId"));
        } catch (Exception nfe) {
            return error;
        }


        CertificatePojo cPojo = engineConfiguration.getCertificateByNxCertificateId(CertificateType.CA.getOrdinal(), nxCertificateId);
        if (cPojo == null) {
            return error;
        }
        byte[] data = cPojo.getBinaryData();
        X509Certificate x509Certificate = CertificateUtil.getX509Certificate(data);
        form.setCertificateProperties(x509Certificate);
        form.setAlias(cPojo.getName());
        form.setNxCertificateId(cPojo.getNxCertificateId());

        List<CertificatePojo> duplicates = CertificateUtil.getDuplicates(engineConfiguration, CertificateType.CA, x509Certificate);
        List<CertificatePropertiesForm> duplicateForms = new ArrayList<>();
        CertificatePropertiesForm duplicateForm;
        for (CertificatePojo duplicate : duplicates) {
            if (duplicate.getNxCertificateId() != nxCertificateId) {
                X509Certificate duplicateCert = CertificateUtil.getX509Certificate(duplicate);
                duplicateForm = new CertificatePropertiesForm();
                duplicateForm.setAlias(duplicate.getName());
                duplicateForm.setCertificateProperties(duplicateCert);
                duplicateForm.setDuplicateFingerprint(CertificateUtil.hasSameMD5FingerPrint(x509Certificate, duplicateCert));
                duplicateForm.setDuplicateSHA1Fingerprint(CertificateUtil.hasSameSHA1FingerPrint(x509Certificate, duplicateCert));
                duplicateForm.setDuplicateDistinguishedName(CertificateUtil.hasSameDistinguishedName(x509Certificate, duplicateCert));
                duplicateForm.setDuplicateSki(CertificateUtil.hasSameSubjectKeyIdentifier(x509Certificate, duplicateCert));
                duplicateForm.setNxCertificateId(duplicate.getNxCertificateId());
                duplicateForms.add(duplicateForm);
            }
        }
        request.setAttribute("duplicates", duplicateForms);


        if (!errors.isEmpty()) {
            return error;
        }
        // request.getSession().setAttribute( Crumbs.CURRENT_LOCATION, Crumbs.CA_VIEW );

        return success;
    }

}
