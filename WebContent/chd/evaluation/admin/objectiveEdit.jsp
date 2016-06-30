<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table  width="100%" border="0" cellpadding="0" cellspacing="3">
	<col valign="middle" align="right">
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap">年度：</td>
		<td><ext:field property="objective.objectiveYear"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">目标：</td>
		<td><ext:field property="objective.objective"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">完成情况：</td>
		<td><ext:field property="objective.result"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">备注：</td>
		<td><ext:field property="objective.remark"/></td>
	</tr>
</table>