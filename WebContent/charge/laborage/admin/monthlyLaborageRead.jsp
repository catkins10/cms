<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="100px" class="tdtitle">
	<col valign="middle" width="50%" class="tdcontent">
	<col valign="middle" width="100px" class="tdtitle">
	<col valign="middle" width="50%" class="tdcontent">
	<tr>
		<td>总金额：</td>
		<td><ext:write property="totalLaborage"/>元</td>
		<td>单据总数：</td>
		<td><ext:write property="paymentCount"/></td>
	</tr>
	<tr>
		<td>税率：</td>
		<td><ext:write property="taxRateTitle"/></td>
		<td>支付金额：</td>
		<td><ext:write property="moneyPay"/>元</td>
	</tr>
	<tr>
		<td>银行：</td>
		<td><ext:write property="bank"/></td>
		<td>银行账户名：</td>
		<td><ext:write property="bankAccountName"/></td>
	</tr>
	<tr>
		<td>银行帐号：</td>
		<td colspan="3"><ext:write property="bankAccount"/></td>
	</tr>
	<tr>
		<td>账单开始时间：</td>
		<td><ext:write property="beginDate" format="yyyy-MM-dd"/></td>
		<td>账单结束时间</td>
		<td><ext:write property="endDate" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td>支付时间：</td>
		<td colspan="3"><ext:write property="payTime" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
	<tr>
		<td>备注：</td>
		<td colspan="3"><ext:write property="remark"/></td>
	</tr>
</table>