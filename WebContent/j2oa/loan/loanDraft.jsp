<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请人</td>
		<td class="tdcontent"><ext:field property="loanPersonName"/></td>
		<td class="tdtitle" nowrap="nowrap">申请部门</td>
		<td class="tdcontent"><ext:field property="loanDepartmentName"/></td>
		<td class="tdtitle" nowrap="nowrap">费用类别</td>
		<td class="tdcontent"><ext:field property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top" style="padding-top:6px">申请事由</td>
		<td colspan="5" class="tdcontent"><ext:field property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请金额</td>
		<td class="tdcontent"><ext:field property="money" onfocus="select()" onchange="document.getElementById('tdMoneyCapital').innerHTML = StringUtils.getMoneyCapital(value)"/></td>
		<td class="tdtitle" nowrap="nowrap">金额(大写)</td>
		<td colspan="3" id="tdMoneyCapital" class="tdcontent"><ext:field property="moneyCapital"/></td>
	</tr>
	<logic:greaterThan value="0" name="loan" property="reimburseMoney">
		<tr>
			<td class="tdtitle" nowrap="nowrap">已核销金额</td>
			<td class="tdcontent"><ext:field property="reimburseMoney"/></td>
			<td class="tdtitle" nowrap="nowrap">大写</td>
			<td colspan="3" id="tdMoneyCapital" class="tdcontent"><ext:field property="reimburseMoneyCapital"/></td>
		</tr>
	</logic:greaterThan>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请时间</td>
		<td class="tdcontent"><ext:field property="loanDate"/></td>
		<td class="tdtitle" nowrap="nowrap">预计还款时间</td>
		<td colspan="3" class="tdcontent"><ext:field property="intendingRepayDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">支付时间</td>
		<td class="tdcontent"><ext:field property="payDate"/></td>
		<td class="tdtitle" nowrap="nowrap">实际还款时间</td>
		<td colspan="3" class="tdcontent"><ext:field property="repayDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="5" class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>