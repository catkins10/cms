<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doReviseInfo">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap" valign="top">退改稿意见：</td>
			<td><ext:field property="reviseOpinion"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">联系电话：</td>
			<td><ext:field property="revisePersonTel"/></td>
		</tr>
	</table>
</ext:form>