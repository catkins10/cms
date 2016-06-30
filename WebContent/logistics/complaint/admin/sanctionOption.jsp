<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" cellspacing="0" cellpadding="3" width="300px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">将被投诉的货源(车源)删除：</td>
		<td><ext:field property="deleteSupply"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">将被投诉的公司(个人)加入黑名单：</td>
		<td><ext:field property="addBlacklist"/></td>
	</tr>
</table>