<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="100px" align="right">
	<col width="100%">
	<tr>
		<td>年份：</td>
		<td><ext:write property="projectPlan.planYear"/></td>
	</tr>
	<tr>
		<td>月份：</td>
		<td><ext:write property="projectPlan.planMonth"/></td>
	</tr>
	<tr>
		<td>新进场单位：</td>
		<td><ext:write property="projectPlan.enterUnit"/></td>
	</tr>
	<tr>
		<td valign="top">工作安排及需要调解决的意见：</td>
		<td><html:textarea property="projectPlan.plan" style="height:110px" readonly="true"/></td>
	</tr>
</table>