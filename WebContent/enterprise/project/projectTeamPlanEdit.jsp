<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="100px" align="right">
	<col width="100%">
	<tr>
		<td>项目组成员：</td>
		<td><ext:field property="plan.memberNames"/></td>
	</tr>
	<tr>
		<td valign="top">工作内容：</td>
		<td><ext:field property="plan.workContent"/></td>
	</tr>
	<tr>
		<td>计划开始时间：</td>
		<td><ext:field property="plan.beginDate"/></td>
	</tr>
	<tr>
		<td>计划结束时间：</td>
		<td><ext:field property="plan.endDate"/></td>
	</tr>
	<tr>
		<td>实际完成时间：</td>
		<td><ext:field property="plan.completionDate"/></td>
	</tr>
	<tr>
		<td valign="top">实际完成情况：</td>
		<td><ext:field property="plan.completionDescription"/></td>
	</tr>
</table>