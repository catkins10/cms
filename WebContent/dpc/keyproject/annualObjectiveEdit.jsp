<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td>年度：</td>
		<td><ext:field property="annualObjective.objectiveYear"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">计划完成投资(万元)：</td>
		<td><ext:field property="annualObjective.investPlan"/></td>
	</tr>
	<tr>
		<td valign="top">形象进度目标：</td>
		<td><ext:field property="annualObjective.objective"/></td>
	</tr>
</table>