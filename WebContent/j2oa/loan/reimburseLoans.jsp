<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table" class="view">
	<tr height="20px" align="center">
		<td class="viewHeader" width="100%">报销事由</td>     
		<td class="viewHeader" width="80px">报销时间</td>
		<td class="viewHeader" width="80px">报销金额</td>
		<td class="viewHeader" width="80px">核销金额</td>
		<td class="viewHeader" width="110px">核销时间</td>
	</tr>
	<ext:iterate id="reimburseLoan" indexId="reimburseLoanIndex" property="reimburseLoans">
		<tr class="viewRow">
			<td class="viewColOdd"><a href="javascript:PageUtils.editrecord('j2oa/reimburse', 'reimburse', <ext:write name="reimburseLoan" property="reimburseId"/>, 'width=760,height=520');"><ext:write name="reimburseLoan" property="reimburse.description"/></a></td>
			<td class="viewColEven" align="center"><ext:write name="reimburseLoan" property="reimburse.reimburseDate" format="yyyy-MM-dd"/></td>
			<td class="viewColOdd" align="center"><ext:write name="reimburseLoan" property="reimburse.money"/></td>
			<td class="viewColEven" align="center"><ext:write name="reimburseLoan" property="reimburseMoney"/></td>
			<td class="viewColOdd" align="center"><ext:write name="reimburseLoan" property="reimburseTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</ext:iterate>
</table>