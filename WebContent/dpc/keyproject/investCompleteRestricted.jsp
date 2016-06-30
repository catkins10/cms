<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
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
		<td>旬：</td>
		<td><ext:field writeonly="true" property="investComplete.completeTenDay"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">计划完成投资（万元）：</td>
		<td><ext:field writeonly="true" property="investComplete.investPlan"/></td>
	</tr>
	<tr>
		<td>完成投资（万元）：</td>
		<td><ext:field property="investComplete.completeInvest"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">年初至报告期累计完成投资：</td>
		<td><ext:field property="investComplete.yearInvest"/>万元</td>
	</tr>
	<tr>
		<td>占年计划：</td>
		<td><ext:field property="investComplete.percentage"/>%</td>
	</tr>
	<tr>
		<td nowrap="nowrap">开工至报告期累计完成投资：</td>
		<td><ext:field property="investComplete.totalComplete"/>万元</td>
	</tr>
	<tr>
		<td>占总投资：</td>
		<td><ext:field property="investComplete.completePercentage"/>%</td>
	</tr>
</table>