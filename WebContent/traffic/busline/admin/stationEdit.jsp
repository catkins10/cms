<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">站点名称：</td>
		<td><ext:field property="station.name"/></td>
	</tr>
	<tr>
		<td>行驶方向：</td>
		<td><ext:field property="station.direction"/></td>
	</tr>
	<tr>
		<td>站点性质：</td>
		<td><ext:field property="station.status"/></td>
	</tr>
</table>