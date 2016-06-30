<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveLeaderMailDepartment">
   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<col>
		<col valign="middle" width="100%">
		<tr>
			<td nowrap="nowrap">受理部门：</td>
			<td><ext:field property="departments"/></td>
		</tr>
	</table>
</ext:form>