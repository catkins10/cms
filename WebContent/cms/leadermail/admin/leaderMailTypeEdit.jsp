<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" cellpadding="3" cellspacing="0">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" align="right">类型：</td>
		<td><ext:field property="type"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">办理时限：</td>
		<td><ext:field property="workingDay"/></td>
	</tr>
</table>