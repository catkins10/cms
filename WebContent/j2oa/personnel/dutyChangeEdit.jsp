<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">时间：</td>
		<td><ext:field property="dutyChange.changeDate"/></td>
	</tr>
	<tr>
		<td>历史岗位：</td>
		<td><ext:field property="dutyChange.previousDuty"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">现在岗位：</td>
		<td><ext:field property="dutyChange.newDuty"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">岗位变动原因：</td>
		<td><ext:field property="dutyChange.changeReason"/></td>
	</tr>
	<tr>
		<td>备注：</td>
		<td><ext:field property="dutyChange.remark"/></td>
	</tr>
</table>