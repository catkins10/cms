<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function updateReimburseLoanMoney() { //更新核销金额列表
	var reimburseMoneyInputs = document.getElementsByName("reimburseMoney");
	var loanIds = document.getElementsByName("loan.id");
	var money = "";
	for(var i=0; i<loanIds.length; i++) {
		reimburseMoneyInputs[i].value = (isNaN(new Number(reimburseMoneyInputs[i].value)) ? "0.0" : reimburseMoneyInputs[i].value);
		money += (money=="" ? "" : ",") + loanIds[i].value + "," + reimburseMoneyInputs[i].value;
	}
	document.getElementsByName("reimburseLoanMoney")[0].value = money;
}
</script>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td nowrap="nowrap" class="tdtitle" width="100%">借款事由</td>     
		<td nowrap="nowrap" class="tdtitle" width="80px">借款时间</td>
		<td nowrap="nowrap" class="tdtitle" width="80px">支付时间</td>
		<td nowrap="nowrap" class="tdtitle" width="80px">借款金额</td>
		<td nowrap="nowrap" class="tdtitle" width="80px">已核销金额</td>
		<td nowrap="nowrap" class="tdtitle" width="80px">核销金额</td>
	</tr>
	<ext:iterate id="reimburseLoan" indexId="reimburseLoanIndex" property="reimburseLoans">
		<tr>
			<td class="tdcontent"><a href="javascript:PageUtils.editrecord('j2oa/loan', 'loan', <ext:write name="reimburseLoan" property="loan.id"/>, 'width=760,height=520');"><ext:write name="reimburseLoan" property="loan.reason"/></a></td>
			<td class="tdcontent" align="center"><ext:write name="reimburseLoan" property="loan.loanDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent" align="center"><ext:write name="reimburseLoan" property="loan.payDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent" align="center"><ext:write name="reimburseLoan" property="loan.money"/></td>
			<td class="tdcontent" align="center"><ext:write name="reimburseLoan" property="loan.reimburseMoney"/></td>
			<td class="tdcontent" align="center"><html:text name="reimburseLoan" property="reimburseMoney" onfocus="select()" onchange="updateReimburseLoanMoney()"/><html:hidden name="reimburseLoan" property="loan.id"/></td>
		</tr>
	</ext:iterate>
</table>
<html:hidden property="reimburseLoanMoney"/>