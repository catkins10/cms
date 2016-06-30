<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveAssessActivity">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
		<col width="80px" align="right">
		<col width="100%">
		<tr>
			<td>考核步骤：</td>
			<td><ext:field property="activity.activity"/></td>
		</tr>
		<tr>
			<td>权重：</td>
			<td><ext:field property="activity.weight"/></td>
		</tr>
	</table>
</ext:form>