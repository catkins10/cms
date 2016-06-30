<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">第几个五年：</td>
		<td><ext:field writeonly="true" property="projectFiveYearPlan.fiveYearPlanNumber"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">上个五年计划完成投资(万元)：</td>
		<td><ext:field writeonly="true" property="projectFiveYearPlan.previousFiveYearInvest"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">当前五年计划规划投资(万元)：</td>
		<td><ext:field writeonly="true" property="projectFiveYearPlan.currentFiveYearInvest"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">当前五年计划工作目标：</td>
		<td><ext:field writeonly="true" property="projectFiveYearPlan.currentFiveYearObjective"/></td>
	</tr>
</table>