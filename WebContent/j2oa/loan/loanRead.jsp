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
		<td class="tdcontent"><ext:write property="loanPersonName"/></td>
		<td class="tdtitle" nowrap="nowrap">申请部门</td>
		<td class="tdcontent"><ext:write property="loanDepartmentName"/></td>
		<td class="tdtitle" nowrap="nowrap">费用类别</td>
		<td class="tdcontent"><ext:write property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top" style="padding-top:6px">申请事由</td>
		<td colspan="5" class="tdcontent"><ext:write property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请金额</td>
		<td class="tdcontent"><ext:write property="money"/></td>
		<td class="tdtitle" nowrap="nowrap">大写</td>
		<td colspan="3" id="tdMoneyCapital" class="tdcontent"><ext:write filter="" property="moneyCapital"/></td>
	</tr>
	<logic:greaterThan value="0" name="loan" property="reimburseMoney">
		<tr>
			<td class="tdtitle" nowrap="nowrap">已核销金额</td>
			<td class="tdcontent"><ext:write property="reimburseMoney"/></td>
			<td class="tdtitle" nowrap="nowrap">大写</td>
			<td colspan="3" id="tdMoneyCapital" class="tdcontent"><ext:write filter="" property="reimburseMoneyCapital"/></td>
		</tr>
	</logic:greaterThan>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请时间</td>
		<td class="tdcontent"><ext:write property="loanDate" format="yyyy-MM-dd"/></td>
		<td class="tdtitle" nowrap="nowrap">预计还款时间</td>
		<td colspan="3" class="tdcontent"><ext:write property="intendingRepayDate" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">支付时间</td>
		<td class="tdcontent"><ext:write property="payDate" format="yyyy-MM-dd"/></td>
		<td class="tdtitle" nowrap="nowrap">实际还款时间</td>
		<td colspan="3" class="tdcontent"><ext:write property="repayDate" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="5" class="tdcontent"><ext:write property="remark"/></td>
	</tr>
</table>