<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">姓名：</td>
		<td><ext:field property="name"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">性别：</td>
		<td><ext:field property="sex"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">执法证发证日期：</td>
		<td><ext:field property="certificateDate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">执法证编号：</td>
		<td><ext:field property="certificateNumber"/></td>
	</tr>
</table>