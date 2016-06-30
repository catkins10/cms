<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
11
<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">年份：</td>
		<td><ext:field writeonly="true" property="progress.progressYear"/></td>
	</tr>
	<tr>
		<td>月份：</td>
		<td><ext:field writeonly="true" property="progress.progressMonth"/></td>
	</tr>
	<tr>
		<td>旬：</td>
		<td><ext:field writeonly="true" property="progress.progressTenDay"/></td>
	</tr>
	<tr>
		<td valign="top" nowrap="nowrap">进度安排：</td>
		<td><ext:field property="progress.plan" readonly="true"/></td>
	</tr>
	<tr>
		<td valign="top" nowrap="nowrap">完成情况：</td>
		<td><ext:field property="progress.progress"/></td>
	</tr>
</table>