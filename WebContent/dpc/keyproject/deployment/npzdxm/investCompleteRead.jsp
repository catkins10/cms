<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="230px" align="right">
	<col width="100%">
	<tr>
		<td>年份：</td>
		<td><ext:field writeonly="true" property="investComplete.completeYear"/></td>
	</tr>
	<tr>
		<td>月份：</td>
		<td><ext:field writeonly="true" property="investComplete.completeMonth"/></td>
	</tr>
	<tr>
		<td>当月完成投资（万元）：</td>
		<td><ext:field writeonly="true" property="investComplete.completeInvest"/></td>
	</tr>
	<tr>
		<td>年初至报告期累计完成投资（万元）：</td>
		<td><ext:field writeonly="true" property="investComplete.yearInvest"/></td>
	</tr>
	<tr>
		<td>占年计划（%）：</td>
		<td><ext:field writeonly="true" property="investComplete.percentage"/></td>
	</tr>
	<tr>
		<td>开工至报告期累计完成投资（万元）：</td>
		<td><ext:field writeonly="true" property="investComplete.totalComplete"/></td>
	</tr>
	<tr>
		<td>占总投资（%）：</td>
		<td><ext:field writeonly="true" property="investComplete.completePercentage"/></td>
	</tr>
</table>