<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table  width="100%" border="0" cellpadding="0" cellspacing="3">
	<col valign="middle" align="right">
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap">年度：</td>
		<td><ext:field property="honor.honorYear"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">荣誉名称：</td>
		<td><ext:field property="honor.honor"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">授予单位：</td>
		<td><ext:field property="honor.honorAwarding"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">授予时间：</td>
		<td><ext:field property="honor.honorDate"/></td>
	</tr>
</table>