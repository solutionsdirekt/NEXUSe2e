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
<%@ taglib uri="/tags/nexus" prefix="nexus"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/tags/struts-html-el" prefix="html-el"%>

<%@page import="java.util.Calendar"%>

<% /* <nexus:helpBar /> */ %>

<center><script language="JavaScript" type="text/javascript">
      	this.clearConvId = function () {
      		document.messageSubmissionForm.conversationId.value='';
      	}
      </script>

<table class="NEXUS_TABLE" width="100%">
	<tr>
		<td><nexus:crumbs styleClass="NEXUSScreenPathLink"></nexus:crumbs></td>
	</tr>
	<tr>
		<td class="NEXUSScreenName">Database Purge</td>
	</tr>
</table>

<html:form action="DatabasePurge.do" method="post" enctype="multipart/form-data">
	<logic:equal name="databasePurgeForm" property="type" value="select">
	<table width="100%">
		<tr>
			<td colspan="4" class="NEXUSSection">Parameters for purging</td>
		</tr>
		
		
		<tr>
                <td class="NEXUSValue">Start Date <html:checkbox property="startEnabled"/></td>
                <td class="NEXUSValue" align="left">
                <html:select property="startYear">
                    <%
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.YEAR, 2);
					for (int i = 0; i < 10; i++) {
						request.setAttribute("year", new Integer(cal.get(Calendar.YEAR)));
						cal.add(Calendar.YEAR, -1);
					%>
	                	<html-el:option value="${year}" />
	                <%
					}
	                %>
                </html:select>
                <html:select property="startMonth">
                    <html:option value="01">January</html:option>
                    <html:option value="02">February</html:option>
                    <html:option value="03">March</html:option>
                    <html:option value="04">April</html:option>
                    <html:option value="05">May</html:option>
                    <html:option value="06">June</html:option>
                    <html:option value="07">July</html:option>
                    <html:option value="08">August</html:option>
                    <html:option value="09">September</html:option>
                    <html:option value="10">October</html:option>
                    <html:option value="11">November</html:option>
                    <html:option value="12">December</html:option>
                </html:select>
                <html:select property="startDay">
                  <html:option value="01">1</html:option>
                  <html:option value="02">2</html:option>
                  <html:option value="03">3</html:option>
                  <html:option value="04">4</html:option>
                  <html:option value="05">5</html:option>
                  <html:option value="06">6</html:option>
                  <html:option value="07">7</html:option>
                  <html:option value="08">8</html:option>
                  <html:option value="09">9</html:option>
                  <html:option value="10">10</html:option>
                  <html:option value="11">11</html:option>
                  <html:option value="12">12</html:option>
                  <html:option value="13">13</html:option>
                  <html:option value="14">14</html:option>
                  <html:option value="15">15</html:option>
                  <html:option value="16">16</html:option>
                  <html:option value="17">17</html:option>
                  <html:option value="18">18</html:option>
                  <html:option value="19">19</html:option>
                  <html:option value="20">20</html:option>
                  <html:option value="21">21</html:option>
                  <html:option value="22">22</html:option>
                  <html:option value="23">23</html:option>
                  <html:option value="24">24</html:option>
                  <html:option value="25">25</html:option>
                  <html:option value="26">26</html:option>
                  <html:option value="27">27</html:option>
                  <html:option value="28">28</html:option>
                  <html:option value="29">29</html:option>
                  <html:option value="30">30</html:option>
                  <html:option value="31">31</html:option>
                </html:select>
                <html:select property="startHour">
                    <html:option value="00">12 A.M.</html:option>
                    <html:option value="01">1 A.M.</html:option>
                    <html:option value="02">2 A.M.</html:option>
                    <html:option value="03">3 A.M.</html:option>
                    <html:option value="04">4 A.M.</html:option>
                    <html:option value="05">5 A.M.</html:option>
                    <html:option value="06">6 A.M.</html:option>
                    <html:option value="07">7 A.M.</html:option>
                    <html:option value="08">8 A.M.</html:option>
                    <html:option value="09">9 A.M.</html:option>
                    <html:option value="10">10 A.M.</html:option>
                    <html:option value="11">11 A.M.</html:option>
                    <html:option value="12">12 P.M.</html:option>
                    <html:option value="13">1 P.M.</html:option>
                    <html:option value="14">2 P.M.</html:option>
                    <html:option value="15">3 P.M.</html:option>
                    <html:option value="16">4 P.M.</html:option>
                    <html:option value="17">5 P.M.</html:option>
                    <html:option value="18">6 P.M.</html:option>
                    <html:option value="19">7 P.M.</html:option>
                    <html:option value="20">8 P.M.</html:option>
                    <html:option value="21">9 P.M.</html:option>
                    <html:option value="22">10 P.M.</html:option>
                    <html:option value="23">11 P.M.</html:option>
                </html:select>
              <html:select property="startMin">
              <html:option value="00">0</html:option>
               <html:option value="10">10</html:option>
                 <html:option value="20">20</html:option>
                 <html:option value="30">30</html:option>
                 <html:option value="40">40</html:option>
                 <html:option value="50">50</html:option>
                 
                </html:select></td>
              
              <td class="NEXUSValue">End Date <html:checkbox property="endEnabled"/></td>
                <td class="NEXUSValue" align="left">
                <html:select property="endYear">
                    <%
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.YEAR, 2);
					for (int i = 0; i < 10; i++) {
						request.setAttribute("year", new Integer(cal.get(Calendar.YEAR)));
						cal.add(Calendar.YEAR, -1);
					%>
	                	<html-el:option value="${year}" />
	                <%
					}
	                %>
                </html:select>
                <html:select property="endMonth">
                    <html:option value="01">January</html:option>
                    <html:option value="02">February</html:option>
                    <html:option value="03">March</html:option>
                    <html:option value="04">April</html:option>
                    <html:option value="05">May</html:option>
                    <html:option value="06">June</html:option>
                    <html:option value="07">July</html:option>
                    <html:option value="08">August</html:option>
                    <html:option value="09">September</html:option>
                    <html:option value="10">October</html:option>
                    <html:option value="11">November</html:option>
                    <html:option value="12">December</html:option>
                </html:select>
                <html:select property="endDay">
                  <html:option value="01">1</html:option>
                  <html:option value="02">2</html:option>
                  <html:option value="03">3</html:option>
                  <html:option value="04">4</html:option>
                  <html:option value="05">5</html:option>
                  <html:option value="06">6</html:option>
                  <html:option value="07">7</html:option>
                  <html:option value="08">8</html:option>
                  <html:option value="09">9</html:option>
                  <html:option value="10">10</html:option>
                  <html:option value="11">11</html:option>
                  <html:option value="12">12</html:option>
                  <html:option value="13">13</html:option>
                  <html:option value="14">14</html:option>
                  <html:option value="15">15</html:option>
                  <html:option value="16">16</html:option>
                  <html:option value="17">17</html:option>
                  <html:option value="18">18</html:option>
                  <html:option value="19">19</html:option>
                  <html:option value="20">20</html:option>
                  <html:option value="21">21</html:option>
                  <html:option value="22">22</html:option>
                  <html:option value="23">23</html:option>
                  <html:option value="24">24</html:option>
                  <html:option value="25">25</html:option>
                  <html:option value="26">26</html:option>
                  <html:option value="27">27</html:option>
                  <html:option value="28">28</html:option>
                  <html:option value="29">29</html:option>
                  <html:option value="30">30</html:option>
                  <html:option value="31">31</html:option>
                
                </html:select>
                <html:select property="endHour">
                    <html:option value="00">12 A.M.</html:option>
                    <html:option value="01">1 A.M.</html:option>
                    <html:option value="02">2 A.M.</html:option>
                    <html:option value="03">3 A.M.</html:option>
                    <html:option value="04">4 A.M.</html:option>
                    <html:option value="05">5 A.M.</html:option>
                    <html:option value="06">6 A.M.</html:option>
                    <html:option value="07">7 A.M.</html:option>
                    <html:option value="08">8 A.M.</html:option>
                    <html:option value="09">9 A.M.</html:option>
                    <html:option value="10">10 A.M.</html:option>
                    <html:option value="11">11 A.M.</html:option>
                    <html:option value="12">12 P.M.</html:option>
                    <html:option value="13">1 P.M.</html:option>
                    <html:option value="14">2 P.M.</html:option>
                    <html:option value="15">3 P.M.</html:option>
                    <html:option value="16">4 P.M.</html:option>
                    <html:option value="17">5 P.M.</html:option>
                    <html:option value="18">6 P.M.</html:option>
                    <html:option value="19">7 P.M.</html:option>
                    <html:option value="20">8 P.M.</html:option>
                    <html:option value="21">9 P.M.</html:option>
                    <html:option value="22">10 P.M.</html:option>
                    <html:option value="23">11 P.M.</html:option>
                </html:select>
                <html:select property="endMin">
              <html:option value="00">0</html:option>
               <html:option value="10">10</html:option>
                 <html:option value="20">20</html:option>
                 <html:option value="30">30</html:option>
                 <html:option value="40">40</html:option>
                 <html:option value="50">50</html:option>
                 
                </html:select></td>
            </tr>
        	
	</table>
	</logic:equal>
	
	
	<logic:equal name="databasePurgeForm" property="type" value="preview">
		<input name="startEnabled" value="${databasePurgeForm.startEnabled }" type="hidden">
		<input name="endEnabled" value="${databasePurgeForm.endEnabled }" type="hidden">
		
		<input name="endHour" value="${databasePurgeForm.endHour }" type="hidden">
		<input name="endMin" value="${databasePurgeForm.endMin }" type="hidden">
		<input name="endYear" value="${databasePurgeForm.endYear }" type="hidden">
		<input name="endDay" value="${databasePurgeForm.endDay }" type="hidden">
		<input name="endMonth" value="${databasePurgeForm.endMonth }" type="hidden">
		
		<input name="startHour" value="${databasePurgeForm.startHour }" type="hidden">
		<input name="startMin" value="${databasePurgeForm.startMin }" type="hidden">
		<input name="startYear" value="${databasePurgeForm.startYear }" type="hidden">
		<input name="startDay" value="${databasePurgeForm.startDay }" type="hidden">
		<input name="startMonth" value="${databasePurgeForm.startMonth }" type="hidden">
			
		<table width="100%">
			<tr>
				<td colspan="2" class="NEXUSSection">Warning! Following number of log entries will be purged!</td>
			</tr>
			<tr>
				<td class="NEXUSName">LogEntries:</td><td class="NEXUSValue">${databasePurgeForm.logEntryCount}</td>
			</tr>
		</table>
		
	</logic:equal>
	<table class="NEXUS_BUTTON_TABLE" width="100%">
		<tr>
		
			<input name="type" value="blank" type="hidden">
			<td class="BUTTON_RIGHT">
				<logic:equal name="databasePurgeForm" property="type" value="select">
					<nexus:submit onClick="document.databasePurgeForm.type.value='preview';" styleClass="button"><img src="images/icons/tick.png" class="button">Preview</nexus:submit>
				</logic:equal>
				<logic:equal name="databasePurgeForm" property="type" value="preview">
					<nexus:submit onClick="document.databasePurgeForm.type.value='remove';" styleClass="button"><img src="images/icons/tick.png" class="button">Delete</nexus:submit>
				</logic:equal>
			</td>
		</tr>
	</table>
	</html:form>

</center>
<center><logic:messagesPresent>
	<div class="NexusError"><html:errors /></div>
</logic:messagesPresent> <logic:messagesPresent message="true">
	<html:messages id="msg" message="true">
		<div class="NexusMessage"><bean:write name="msg" /></div>
		<br />
	</html:messages>
</logic:messagesPresent></center>
