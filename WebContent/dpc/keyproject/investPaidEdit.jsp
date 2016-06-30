<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/dpc/keyproject/js/keyproject.js"></script>
<script>
	function selectProjectInvestSource() {
		selectInvestSource(500, 350, false, '', 'setInvestSource("{fullName}")', '', '', '', true);
	}
	function setInvestSource(industry) { //设置资金来源
		var values = industry.split("/");
		document.getElementsByName("investPaid.source")[0].value = values[1];
		document.getElementsByName("investPaid.childSource")[0].value = (values[2] ? values[2] : "");
		document.getElementsByName("investPaid.fullSource")[0].value = values[1] + (values[2] ? "/" + values[2] : "")
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="230px" align="right">
	<col width="100%">
	<tr>
		<td>年份：</td>
		<td><ext:field property="investPaid.paidYear"/></td>
	</tr>
	<tr>
		<td>月份：</td>
		<td><ext:field property="investPaid.paidMonth"/></td>
	</tr>
	<tr>
		<td>当月到位资金金额：</td>
		<td><ext:field property="investPaid.paidInvest"/></td>
	</tr>
	<tr>
		<td>年初至报告期累计到位资金（万元）：</td>
		<td><ext:field property="investPaid.yearInvest"/></td>
	</tr>
	<tr>
		<td>占年计划（%）：</td>
		<td><ext:field property="investPaid.percentage"/></td>
	</tr>
	<tr>
		<td>资金来源：</td>
		<td><ext:field property="investPaid.fullSource"/></td>
	</tr>
	<tr>
		<td>来源说明：</td>
		<td><ext:field property="investPaid.remark"/></td>
	</tr>
</table>