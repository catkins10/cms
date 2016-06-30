<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table  width="100%" border="0" cellpadding="0" cellspacing="3">
	<col valign="middle" align="right">
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap">等级：</td>
		<td><ext:field property="level.level"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">描述：</td>
		<td><ext:field property="level.description"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">奖励：</td>
		<td><ext:field property="level.award"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">备注：</td>
		<td><ext:field property="level.remark"/></td>
	</tr>
</table>