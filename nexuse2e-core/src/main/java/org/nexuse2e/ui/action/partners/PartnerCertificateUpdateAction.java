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
package org.nexuse2e.ui.action.partners;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
import org.nexuse2e.pojo.PartnerPojo;
import org.nexuse2e.ui.action.NexusE2EAction;
import org.nexuse2e.ui.form.CertificatePromotionForm;
import org.nexuse2e.ui.form.CertificatePropertiesForm;
import org.nexuse2e.ui.form.PartnerCertificateForm;
import org.nexuse2e.util.CertificateUtil;
import org.nexuse2e.util.EncryptionUtil;

/**
 * @author gesch
 * 
 */
public class PartnerCertificateUpdateAction extends NexusE2EAction {

    private static String URL     = "partner.error.url";
    private static String TIMEOUT = "partner.error.timeout";

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

        PartnerCertificateForm form = (PartnerCertificateForm) actionForm;

        int nxCertificateId = form.getNxCertificateId();
        int nxPartnerId = form.getNxPartnerId();

        CertificatePojo cert;
        try {
            PartnerPojo partner = engineConfiguration.getPartnerByNxPartnerId(nxPartnerId);
            cert = engineConfiguration.getCertificateFromPartnerByNxCertificateId(partner, nxCertificateId);
        } catch (NexusException e) {
            ActionMessage errorMessage = new ActionMessage("generic.error", e.getMessage());
            errors.add(ActionMessages.GLOBAL_MESSAGE, errorMessage);
            addRedirect(request, URL, TIMEOUT);
            return error;
        }

        byte[] data = cert.getBinaryData();
        X509Certificate x509Certificate = null;
        List<X509Certificate> allCertificates = new ArrayList<X509Certificate>();

        if (cert.getType() == CertificateType.PARTNER.getOrdinal()) {
            x509Certificate = CertificateUtil.getX509Certificate(data);
        } else if (cert.getType() == CertificateType.LOCAL.getOrdinal()) {

            try {
                KeyStore jks = KeyStore.getInstance(CertificateUtil.DEFAULT_KEY_STORE, CertificateUtil.DEFAULT_JCE_PROVIDER);
                jks.load(new ByteArrayInputStream(cert.getBinaryData()), EncryptionUtil.decryptString(cert.getPassword()).toCharArray());
                if (jks != null) {

                    Enumeration<String> aliases = jks.aliases();
                    if (!aliases.hasMoreElements()) {
                        throw new NexusException("no certificate aliases found");
                    }
                    while (aliases.hasMoreElements()) {
                        String tempAlias = aliases.nextElement();
                        if (jks.isKeyEntry(tempAlias)) {
                            java.security.cert.Certificate[] certArray = jks.getCertificateChain(tempAlias);
                            if (certArray != null) {
                                x509Certificate = (X509Certificate) certArray[0];
                                for (int i = 0; i < certArray.length; i++) {
                                    allCertificates.add((X509Certificate) certArray[i]);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new NexusException(e);
            }
        }

        List<CertificatePropertiesForm> certificateList = new ArrayList<CertificatePropertiesForm>();
        if (x509Certificate != null) {
            form.setCertificateProperties(x509Certificate);
        }

        form.setNxCertificateId(cert.getNxCertificateId());
        form.setNxPartnerId(nxPartnerId);
        form.setCertificateId(cert.getName());
        CertificatePromotionForm certs = new CertificatePromotionForm();

        for (CertificatePojo certificate : engineConfiguration.getCertificates(CertificateType.CA.getOrdinal(), null)) {
            byte[] b = certificate.getBinaryData();
            if (b != null && b.length > 0) {
                try {
                    X509Certificate cacert = CertificateUtil.getX509Certificate(b);
                    if (cacert != null) { // #39
                        allCertificates.add(cacert);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        Certificate[] chain = CertificateUtil.getCertificateChain(x509Certificate, allCertificates);
        for (int i = 0; i < chain.length; i++) {
            CertificatePropertiesForm f = new CertificatePropertiesForm();
            X509Certificate certificate = (X509Certificate) chain[i];
            f.setCertificateProperties(certificate);
            certificateList.add(f);

            if (i + 1 == chain.length && !certificate.getSubjectX500Principal().getName().equals(certificate.getIssuerX500Principal().getName())) {
                f = new CertificatePropertiesForm();
                f.setPrincipal(CertificateUtil.getPrincipalFromCertificate(certificate, false));
                f.setNxCertificateId(-1); // indicate "missing"
                f.setFingerprint(CertificateUtil.getPrincipalFromCertificate(certificate, false).getName());
                certificateList.add(f);
            }
        }

        certs.setCertificateParts(certificateList);
        request.setAttribute("certs", certs);

        // request.getSession().setAttribute( Crumbs.CURRENT_LOCATION, Crumbs.PARTNER_ADDCERT + "_" + form.getPartnerId() );

        return success;
    }

}
