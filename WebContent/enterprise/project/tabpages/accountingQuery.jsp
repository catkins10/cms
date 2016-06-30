<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newCollect() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/project/projectCollect.shtml?id=<ext:write property="id"/>&openerTabPage=accounting', 600, 390);
}
function openCollect(collectId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/project/projectCollect.shtml?id=<ext:write property="id"/>&openerTabPage=accounting&collect.id=' + collectId, 600, 390);
}
function newPay() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/project/projectPay.shtml?id=<ext:write property="id"/>&openerTabPage=accounting', 600, 390);
}
function openPay(payId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/project/projectPay.shtml?id=<ext:write property="id"/>&openerTabPage=accounting&pay.id=' + payId, 600, 390);
}
</script>

收款：
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100px">款项名称</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100px">合同编号</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">合同金额</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">应到款数(元)</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">票额(元)</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">开票时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">到款金额(元)</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">到款时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100%">备注</td>
	</tr>
	<ext:iterate id="collect" property="collects">
		<tr style="cursor:pointer" valign="top" align="center" onclick="openCollect('<ext:write name="collect" property="id"/>')">
			<td class="tdcontent" align="left"><ext:write name="collect" property="clauseName"/></td>
			<td class="tdcontent"><ext:write name="collect" property="contract.contractNo"/></td>
			<td class="tdcontent"><ext:write name="collect" property="contract.contractValue" format="#.##"/></td>
			<td class="tdcontent"><ext:write name="collect" property="accountReceivable" format="#.##"/></td>
			<td class="tdcontent"><ext:write name="collect" property="invoiceAmount" format="#.##"/></td>
			<td class="tdcontent"><ext:write name="collect" property="billingDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="collect" property="receiveAmount" format="#.##"/></td>
			<td class="tdcontent"><ext:write name="collect" property="receiveDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="collect" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>

<br>
付款：
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom">
		<td align="center" nowrap="nowrap" class="tdtitle" width="100%">款项名称</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">票额(元)</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100px">收票时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">接收人</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">应付款数(元)</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">是否已付款</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">付款时间</td>
	</tr>
	<ext:iterate id="pay" property="paies">
		<tr align="center" style="cursor:pointer" valign="top" onclick="openPay('<ext:write name="pay" property="id"/>')">
			<td class="tdcontent" align="left"><ext:write name="pay" property="clauseName"/></td>
			<td class="tdcontent"><ext:write name="pay" property="invoiceAmount" format="#.##"/></td>
			<td class="tdcontent"><ext:write name="pay" property="billingReceiveDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="pay" property="receiver"/></td>
			<td class="tdcontent"><ext:write name="pay" property="accountReceivable" format="#.##"/></td>
			<td class="tdcontent"><ext:write name="pay" property="paidStatus"/></td>
			<td class="tdcontent"><ext:write name="pay" property="payDate" format="yyyy-MM-dd"/></td>
		</tr>
	</ext:iterate>
</table>