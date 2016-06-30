<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="120px" align="right">
	<col width="100%">
	<tr>
		<td>加班小时数：</td>
		<td><ext:field property="overtime.onDutyTime"/></td>
	</tr>
	<tr>
		<td>换算的加班天数：</td>
		<td><ext:field property="overtime.absentDay"/></td>
	</tr>
</table>