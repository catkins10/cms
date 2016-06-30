<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newInvestPaid() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/investPaid.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openInvestPaid(investPaidId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/investPaid.shtml?id=<ext:write property="id"/>&investPaid.id=' + investPaidId, 500, 300);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记资金到位情况" onclick="newInvestPaid()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">年份</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">月份</td>
		<td class="tdtitle" nowrap="nowrap">当月到位资金金额</td>
		<td class="tdtitle" nowrap="nowrap">年初至报告期累计到位资金（万元）</td>
		<td class="tdtitle" nowrap="nowrap">占年计划（%）</td>
		<td class="tdtitle" nowrap="nowrap" width="180px">资金来源</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">来源说明</td>
		<td class="tdtitle" nowrap="nowrap">待审核</td>
	</tr>
	<ext:iterate id="investPaid" property="investPaids">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("investPaid")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" align="center" onclick="openInvestPaid('<ext:write name="investPaid" property="id"/>')">
				<td class="tdcontent"><ext:write name="investPaid" property="paidYear"/></td>
				<td class="tdcontent"><ext:write name="investPaid" property="paidMonth"/></td>
				<td class="tdcontent"><ext:write name="investPaid" property="paidInvest" format="#.####"/></td>
				<td class="tdcontent"><ext:write name="investPaid" property="yearInvest" format="#.####"/></td>
				<td class="tdcontent"><ext:write name="investPaid" property="percentage" format="#.##"/>%</td>
				<td class="tdcontent" align="left"><ext:write name="investPaid" property="fullSource"/></td>
				<td class="tdcontent" align="left"><ext:write name="investPaid" property="remark"/></td>
				<td class="tdcontent"><ext:write name="investPaid" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>