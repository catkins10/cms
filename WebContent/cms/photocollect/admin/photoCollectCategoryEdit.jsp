<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">分类：</td>
		<td><ext:field property="category"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">同步的栏目：</td>
		<td><ext:field property="columnNames"/></td>
	</tr>
</table>