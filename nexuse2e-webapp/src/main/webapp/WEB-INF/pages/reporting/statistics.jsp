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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/tags/nexus" prefix="nexus"%>

<% /*<nexus:helpBar helpDoc="documentation/NEXUSe2e.html" /> */ %>

<table class="NEXUS_TABLE" style="width: 100%">
	<tr>
		<td><nexus:crumbs /></td>
	</tr>
	<tr>
		<td class="NEXUSScreenName">Dashboard</td>
	</tr>
</table>

<div class="statistics">
	<h2>Conversations (last 24h)</h2>
	<c:choose>
		<c:when test="${conversationStatusTotal > 0}">
			<div class="chart" id="conversations">
				<logic:iterate id="statusCount" name="conversationStatusCounts">
					<c:set var="percentage" value="${statusCount.value / conversationStatusTotal * 100}"/>
					<c:if test="${percentage > 0}">
						<div class="segment ${statusCount.key}"
							 style="width: ${percentage}%;"
							 title="${statusCount.key} ${statusCount.value}">
							<span class="status-name">
									${statusCount.key}
							</span>
							<span class="count-label">
								${statusCount.value}
							</span>
						</div>
					</c:if>
				</logic:iterate>
			</div>
		</c:when>
		<c:otherwise>
			<table class="NEXUS_TABLE fixed-table">
				<tr>
					<td class="no-data">no conversations</td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>

	<h2>Failed Messages (last 24h)</h2>
	<c:choose>
		<c:when test="${not empty messages}">
			<table class="NEXUS_TABLE fixed-table">
				<colgroup>
					<col>
					<col style="width: 6%">
					<col style="width: 10%">
					<col style="width: 30%">
					<col style="width: 30%">
					<col style="width: 78px">
				</colgroup>
				<tr>
					<th class="NEXUSSection">Time</th>
					<th class="NEXUSSection">Partner</th>
					<th class="NEXUSSection">Choreography</th>
					<th class="NEXUSSection">Conversation</th>
					<th class="NEXUSSection">Message</th>
					<th class="NEXUSSection"></th>
				</tr>
				<logic:iterate id="message" name="messages" indexId="index">
					<c:if test="${index lt 10}" >
						<tr>
							<td title="${message.createdDate}">
									${message.createdDate}
							</td>
							<td title="${message.partnerId}">
									${message.partnerId}
							</td>
							<td title="${message.choreographyId}">
									${message.choreographyId}
							</td>
							<td title="${message.conversationId}">
								<nexus:link
										href="ConversationView.do?convId=${message.nxConversationId}"
										styleClass="NexusLink">
									${message.conversationId}
								</nexus:link>
							</td>
							<td title="${message.messageId}">
								<nexus:link
										href="MessageView.do?mId=${message.messageId}"
										styleClass="NexusLink">
									${message.messageId}
								</nexus:link>
							</td>
							<td>
								<button onClick="this.disabled = true; setContentUrl('ModifyMessage.do?&origin=dashboard&command=requeue&conversationId=${message.conversationId}&messageId=${message.messageId}');">
									Requeue
								</button>
							</td>
						</tr>
					</c:if>
				</logic:iterate>
			</table>
			<c:if test="${fn:length(messages) gt 10}">
				<button onClick="setContentUrl('ProcessConversationReport.do?noReset=true')" class="full-width">
					Show more
				</button>
			</c:if>
		</c:when>
		<c:otherwise>
			<table class="NEXUS_TABLE fixed-table">
				<tr>
					<td class="no-data">no failed messages</td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>

	<h2>Idle (last 24h and > ${idleGracePeriodInMinutes} minutes idle)</h2>
	<c:choose>
		<c:when test="${not empty idleConversations}">
			<table class="NEXUS_TABLE fixed-table">
				<colgroup>
					<col style="width: 25%">
					<col style="width: 40%">
					<col>
					<col>
				</colgroup>
				<tr>
					<th class="NEXUSSection">Idle since</th>
					<th class="NEXUSSection">Conversation</th>
					<th class="NEXUSSection">Partner</th>
					<th class="NEXUSSection">Choreography</th>
				</tr>
				<logic:iterate id="conversation" name="idleConversations" indexId="index">
					<c:if test="${index lt 10}" >
						<tr>
							<td title="${conversation.modifiedDate}">
									${conversation.modifiedDate}
							</td>
							<td title="${conversation.conversationId}">
								<nexus:link
										href="ConversationView.do?convId=${conversation.nxConversationId}"
										styleClass="NexusLink">
									${conversation.conversationId}
								</nexus:link>
							</td>
							<td title="${conversation.partnerId}">
									${conversation.partnerId}
							</td>
							<td title="${conversation.choreographyId}">
									${conversation.choreographyId}
							</td>
						</tr>
					</c:if>
				</logic:iterate>

			</table>
			<c:if test="${fn:length(idleConversations) gt 10}">
				<button onClick="setContentUrl('ProcessConversationReport.do?noReset=true')" class="full-width">
					Show more
				</button>
			</c:if>
		</c:when>
		<c:otherwise>
			<table class="NEXUS_TABLE fixed-table">
				<tr>
					<td class="no-data">no idle conversations</td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>

	<h2>Successful Messages (last ${transactionActivityTimeframeInWeeks} weeks)</h2>

	<div class="tab">
		<button class="tablinks" onclick="openTab(event, 'choreographies')">Choreographies</button>
		<button class="tablinks" onclick="openTab(event, 'partners')">Partners</button>
	</div>

	<table class="NEXUS_TABLE fixed-table tabcontent" id="partners">
		<c:choose>
			<c:when test="${not empty partners}">
				<colgroup>
					<col>
					<col>
					<col>
				</colgroup>
				<tr>
					<th class="NEXUSSection">Partner ID</th>
					<th class="NEXUSSection">
						Last Inbound
						<div class="info-icon" title="Last successfully received message (excluding acknowledgements)">?</div>
					</th>
					<th class="NEXUSSection">
						Last Outbound
						<div class="info-icon" title="Last successfully sent message (excluding acknowledgements)">?</div>
					</th>
				</tr>
				<logic:iterate id="partner" name="partners">
					<tr>
						<td title="${partner.partnerId}"><nexus:link
								href="PartnerInfoView.do?nxPartnerId=${partner.nxPartnerId}&type=2"
								styleClass="NexusLink">
							${partner.partnerId} (${partner.name})
						</nexus:link></td>
						<td title="${partner.lastInboundTime}">
							${partner.lastInboundTime}
						</td>
						<td title="${partner.lastOutboundTime}">
							${partner.lastOutboundTime}
						</td>
					</tr>
				</logic:iterate>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="no-data">no sent messages</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>

	<table class="NEXUS_TABLE fixed-table tabcontent" id="choreographies">
		<c:choose>
			<c:when test="${not empty choreographies}">
			<colgroup>
				<col>
				<col>
				<col>
			</colgroup>
			<tr>
				<th class="NEXUSSection">Choreography</th>
				<th class="NEXUSSection">
					Last Inbound
					<div class="info-icon" title="Last successfully received message (excluding acknowledgements)">?</div>
				</th>
				<th class="NEXUSSection">
					Last Outbound
					<div class="info-icon" title="Last successfully sent message (excluding acknowledgements)">?</div>
				</th>
			</tr>
			<logic:iterate id="choreography" name="choreographies">
				<tr>
					<td title="${choreography.name}"><nexus:link
							href="ChoreographyView.do?nxChoreographyId=${choreography.nxChoreographyId}"
							styleClass="NexusLink">
						${choreography.name}
					</nexus:link></td>
					<td title="${choreography.lastInboundTime}">
						${choreography.lastInboundTime}
					</td>
					<td title="${choreography.lastOutboundTime}">
						${choreography.lastOutboundTime}
					</td>
				</tr>
			</logic:iterate>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="no-data">no sent messages</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>

	<h2 style="display: inline-block;">Certificates</h2>
	<div class="info-icon" title="Certificates referenced in your participant configuration">?</div>
	<table class="NEXUS_TABLE fixed-table" id="certificates">
		<c:choose>
			<c:when test="${not empty certificates}">
				<tr>
					<th class="NEXUSSection">Configured For</th>
					<th class="NEXUSSection">Certificate Name</th>
					<th class="NEXUSSection">Time Remaining</th>
				</tr>
				<logic:iterate id="cert" name="certificates">
					<c:choose>
						<c:when test="${cert.configuredFor == 'Local'}">
							<tr class="local">
								<td class="NEXUSName" title="Local">Local</td>
								<td class="NEXUSName" title="${cert.name}">
									<nexus:link styleClass="NexusLink"
												href="StagingCertificateView.do?nxCertificateId=${cert.nxCertificateId}">
										${cert.name}
									</nexus:link>
								</td>
								<td class="NEXUSName" title="${cert.timeUntilExpiry}">${cert.timeUntilExpiry}</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td title="${cert.configuredFor}">${cert.configuredFor}</td>
								<td title="${cert.name}">
									<nexus:link styleClass="NexusLink"
												href="PartnerCertificateView.do?nxPartnerId=${cert.nxPartnerId}&nxCertificateId=${cert.nxCertificateId}">
										${cert.name}
									</nexus:link>
								</td>
								<td title="${cert.timeUntilExpiry}">${cert.timeUntilExpiry}</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</logic:iterate>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="no-data">no certificates in use</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>

	<div class="reload-button">
		<nexus:link styleClass="button"
					href="ReportingStatistics.do">
			<img src="images/icons/arrow_rotate_anticlockwise.png" name="reloadButton" class="button">Reload
		</nexus:link>
	</div>
</div>


<script>
	function openTab(evt, contentId) {
		var tabContent = document.querySelectorAll(".tabcontent");
		for (var i = 0; i < tabContent.length; i++) {
			tabContent[i].style.display = "none";
		}

	var tabLinks = document.querySelectorAll(".tablinks");
	for (var i = 0; i < tabLinks.length; i++) {
		tabLinks[i].className = tabLinks[i].className.replace(" active", "");
	}

	document.getElementById(contentId).style.display = "table";
	var target = (evt.currentTarget) ? evt.currentTarget : evt.srcElement;
	target.className += " active";
}

/* Open first tab on page load */
document.querySelector('.tablinks').click();
</script>

<logic:messagesPresent>
	<div class="NexusError"><html:errors /></div>
</logic:messagesPresent>
