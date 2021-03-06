<%--

     NEXUSe2e Business Messaging Open Source
     Copyright 2000-2021, direkt gruppe GmbH

     This is free software; you can redistribute it and/or modify it
     under the terms of the GNU Lesser General Public License as
     published by the Free Software Foundation version 3 of
     the License.

     This software is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
     Lesser General Public License for more details.

     You should have received a copy of the GNU Lesser General Public
     License along with this software; if not, write to the Free
     Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
     02110-1301 USA, or see the FSF site: http://www.fsf.org.

--%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/tags/nexus" prefix="nexus"%>

<% /*<nexus:helpBar helpDoc="documentation/Collaboration_Partners.htm" /> */ %>

<table class="NEXUS_TABLE" width="100%">
	<tr>
		<td><nexus:crumbs styleClass="NEXUSScreenPathLink"></nexus:crumbs></td>
	</tr>
	<tr>
		<logic:equal name="collaborationPartnerForm" property="type" value="1">
			<td class="NEXUSScreenName">Add Server Identity</td>
		</logic:equal>
		<logic:equal name="collaborationPartnerForm" property="type" value="2">
			<td class="NEXUSScreenName">Add Collaboration Partner</td>
		</logic:equal>
	</tr>
</table>

<html:form action="CollaborationPartnerCreate">
	<html:hidden property="type" />
	<table class="NEXUS_TABLE" width="100%">
		<tr>
			<td class="NEXUSSection">Partner Id</td>
			<td class="NEXUSSection"><html:text size="50"
				name="collaborationPartnerForm" property="partnerId" /></td>
		</tr>
		<tr>
			<td class="NEXUSSection">Partner Id Type</td>
			<td class="NEXUSSection"><html:text size="50"
				name="collaborationPartnerForm" property="partnerIdType" /></td>
		</tr>
		<tr>
			<td class="NEXUSName">Name</td>
			<td class="NEXUSValue"><html:text property="name" size="50" /></td>
		</tr>
		<tr>
			<td class="NEXUSName">Company</td>
			<td class="NEXUSValue"><html:text property="company" size="50" /></td>
		</tr>
		<tr>
			<td class="NEXUSName">Address Line 1</td>
			<td class="NEXUSValue"><html:text size="50"
				name="collaborationPartnerForm" property="address1" /></td>
		</tr>

		<tr>
			<td class="NEXUSName">Address Line 2</td>
			<td class="NEXUSValue"><html:text size="50"
				name="collaborationPartnerForm" property="address2" /></td>
		</tr>

		<tr>
			<td class="NEXUSName">City</td>
			<td class="NEXUSValue"><html:text size="50"
				name="collaborationPartnerForm" property="city" /></td>
		</tr>

		<tr>
			<td class="NEXUSName">State</td>
			<td class="NEXUSValue"><html:text size="50"
				name="collaborationPartnerForm" property="state" /></td>
		</tr>

		<tr>
			<td class="NEXUSName">Zip Code</td>
			<td class="NEXUSValue"><html:text size="50"
				name="collaborationPartnerForm" property="zip" /></td>
		</tr>

		<tr>
			<td class="NEXUSName">Country</td>
			<td class="NEXUSValue"><html:text size="50"
				name="collaborationPartnerForm" property="country" /></td>
		</tr>
	</table>

	<table class="NEXUS_BUTTON_TABLE">
		<tr>
			<td>&nbsp;</td>
			<td class="NexusHeaderLink" style="text-align: right;"><nexus:submit
				precondition="true /*commPartnerCheckFields()*/" styleClass="button">
				<img src="images/icons/tick.png" class="button">Save</nexus:submit></td>
		</tr>
	</table>
</html:form>
