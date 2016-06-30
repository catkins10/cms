<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8"%>

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
			<td class="tdcontent" align="center"><ext:write name="reimburseLoan" property="reimburseMoney"/></td>
		</tr>
	</ext:iterate>
</table>