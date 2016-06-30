<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">款项名称：</td>
		<td><ext:field property="collect.clauseName"/></td>
	</tr>
	<tr>
		<td>合同：</td>
		<td><ext:field property="contractName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">应到款数(元)：</td>
		<td><ext:field property="collect.accountReceivable"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">开票金额(元)：</td>
		<td><ext:field property="collect.invoiceAmount"/></td>
	</tr>
	<tr>
		<td>开票时间：</td>
		<td><ext:field property="collect.billingDate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">到款金额(元)：</td>
		<td><ext:field property="collect.receiveAmount" /></td>
	</tr>
	<tr>
		<td>到款时间：</td>
		<td><ext:field property="collect.receiveDate"/></td>
	</tr>
	<tr>
		<td>备注：</td>
		<td><ext:field property="collect.remark"/></td>
	</tr>
</table>