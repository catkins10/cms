<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveResponseMessage">
	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col valign="middle" width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">应答目标</td>
			<td class="tdcontent"><ext:field property="responseType" onchange="document.getElementById('trKeywords').style.display = document.getElementsByName('responseType')[0].value=='talk' ? '' : 'none';"/></td>
		</tr>
		<tr id="trKeywords" style="<ext:notEqual value="talk" property="responseType">display:none</ext:notEqual>">
			<td class="tdtitle" nowrap="nowrap">关键字</td>
			<td class="tdcontent"><ext:field property="keywords"/></td>
		</tr>
		<jsp:include page="messageContentEdit.jsp" flush="true"/>
	</table>
</ext:form>