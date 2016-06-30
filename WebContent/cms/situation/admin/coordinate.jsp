<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="430px" border="0" cellpadding="3" cellspacing="0">
	<tr>
		<td nowrap="nowrap" valign="top">协调部门：</td>
		<td width="100%"><ext:field property="coordinate.coordinateUnitName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">协调原因：</td>
		<td width="100%"><ext:field property="coordinate.coordinateReason"/></td>
	</tr>
</table>