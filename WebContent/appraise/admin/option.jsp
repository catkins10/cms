<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveOption">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">类型：</td>
			<td><ext:field property="option.type"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">选项名称：</td>
			<td><ext:field property="option.option"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">短信选项：</td>
			<td><ext:field property="option.smsOption"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">分值：</td>
			<td><ext:field property="option.score"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">是否弃权：</td>
			<td><ext:field property="option.abstain"/></td>
		</tr>
	</table>
</ext:form>