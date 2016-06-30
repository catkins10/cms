<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveEvaluationParameter">
	<table width="100%" border="0" cellpadding="2" cellspacing="0">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">不参与考核的部门：</td>
			<td><ext:field property="dropoutDepartments"/></td>
		</tr>
	</table>
</ext:form>