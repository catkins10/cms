<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveBidEnterprise">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">企业名称：</td>
			<td><ext:field property="name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">帐号：</td>
			<td><ext:field property="account"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">开户行：</td>
			<td><ext:field property="bank"/></td>
		</tr>
	</table>
</ext:form>