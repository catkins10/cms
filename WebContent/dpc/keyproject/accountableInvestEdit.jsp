<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/dpc/keyproject/js/keyproject.js"></script>
<script>
	function selectProjectInvestSource() {
		selectInvestSource(500, 350, false, '', 'setInvestSource("{fullName}")', '', '', '', true);
	}
	function setInvestSource(industry) { //设置资金来源
		var values = industry.split("/");
		document.getElementsByName("projectAccountableInvest.source")[0].value = values[1];
		document.getElementsByName("projectAccountableInvest.childSource")[0].value = (values[2] ? values[2] : "");
		document.getElementsByName("projectAccountableInvest.fullSource")[0].value = values[1] + (values[2] ? "/" + values[2] : "")
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="100px" align="right">
	<col width="100%">
	<tr>
		<td>资金来源：</td>
		<td><ext:field property="projectAccountableInvest.fullSource"/></td>
	</tr>
	<tr>
		<td>资金金额：</td>
		<td><ext:field property="projectAccountableInvest.amount"/></td>
	</tr>
	<tr>
		<td>来源说明：</td>
		<td><ext:field property="projectAccountableInvest.remark"/></td>
	</tr>
</table>