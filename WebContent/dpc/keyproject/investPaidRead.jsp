<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="230px" align="right">
	<col width="100%">
	<tr>
		<td>年份：</td>
		<td><ext:field writeonly="true" property="investPaid.paidYear"/></td>
	</tr>
	<tr>
		<td>月份：</td>
		<td><ext:field writeonly="true" property="investPaid.paidMonth"/></td>
	</tr>
	<tr>
		<td>当月到位资金金额：</td>
		<td><ext:field writeonly="true" property="investPaid.paidInvest"/></td>
	</tr>
	<tr>
		<td>年初至报告期累计到位资金（万元）：</td>
		<td><ext:field writeonly="true" property="investPaid.yearInvest"/></td>
	</tr>
	<tr>
		<td>占年计划（%）：</td>
		<td><ext:field writeonly="true" property="investPaid.percentage"/></td>
	</tr>
	<tr>
		<td>资金来源：</td>
		<td>
			<ext:field writeonly="true" property="investPaid.fullSource"/>
		</td>
	</tr>
	<tr>
		<td>来源说明：</td>
		<td><ext:field writeonly="true" property="investPaid.remark"/></td>
	</tr>
</table>