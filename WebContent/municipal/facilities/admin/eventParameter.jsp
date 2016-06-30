<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveEventParameter">
   	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col valign="middle">
		<col valign="middle" width="100%">
		<tr>
			<td nowrap="nowrap">编号规则：</td>
			<td><html:text property="eventNumberFormat" styleClass="field required"/></td>
		</tr>
	</table>
</ext:form>