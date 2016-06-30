<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveFaq">
	<table width="100%" cellpadding="3" cellspacing="0">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" align="right">问题：</td>
		<td><ext:field property="question"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">关键词：</td>
		<td><ext:field property="keywords"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right" valign="top">答案：</td>
		<td><ext:field property="answer"/></td>
	</tr>
</table>
</ext:form>