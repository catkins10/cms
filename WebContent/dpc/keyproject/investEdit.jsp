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
		document.getElementsByName("projectInvest.source")[0].value = values[1];
		document.getElementsByName("projectInvest.childSource")[0].value = (values[2] ? values[2] : "");
		document.getElementsByName("projectInvest.fullSource")[0].value = values[1] + (values[2] ? "/" + values[2] : "")
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="100px" align="right">
	<col width="100%">
	<tr>
		<td>总投资(万元)：</td>
		<td><ext:field property="projectInvest.amount"/></td>
	</tr>
	<tr>
		<td>资金来源：</td>
		<td><ext:field property="projectInvest.fullSource"/></td>
	</tr>
	<tr>
		<td>来源说明：</td>
		<td><ext:field property="projectInvest.remark"/></td>
	</tr>
</table>