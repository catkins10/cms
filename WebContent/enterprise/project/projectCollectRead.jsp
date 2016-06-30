<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="100px" align="right">
	<col width="100%">
	<tr>
		<td>款项名称：</td>
		<td><ext:field writeonly="true" property="collect.clauseName"/></td>
	</tr>
	<tr>
		<td>合同：</td>
		<td><ext:field writeonly="true" property="contractName"/></td>
	</tr>
	<tr>
		<td>应到款数(元)：</td>
		<td><ext:field writeonly="true" property="collect.accountReceivable"/></td>
	</tr>
	<tr>
		<td>开票金额(元)：</td>
		<td><ext:field writeonly="true" property="collect.invoiceAmount"/></td>
	</tr>
	<tr>
		<td>开票时间：</td>
		<td><ext:field writeonly="true" property="collect.billingDate"/></td>
	</tr>
	<tr>
		<td>到款金额(元)：</td>
		<td><ext:field writeonly="true" property="collect.receiveAmount" /></td>
	</tr>
	<tr>
		<td>到款时间：</td>
		<td><ext:field writeonly="true" property="collect.receiveDate"/></td>
	</tr>
	<tr>
		<td>备注：</td>
		<td><ext:field writeonly="true" property="collect.remark"/></td>
	</tr>
</table>