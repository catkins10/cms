<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="100px" align="right">
	<col width="100%">
	<tr>
		<td>款项名称：</td>
		<td><ext:field writeonly="true" property="pay.clauseName"/></td>
	</tr>
	<tr>
		<td>票额(元)：</td>
		<td><ext:field writeonly="true" property="pay.invoiceAmount"/></td>
	</tr>
	<tr>
		<td>应付款数(元)：</td>
		<td><ext:field writeonly="true" property="pay.accountReceivable"/></td>
	</tr>
	<tr>
		<td>收票时间：</td>
		<td><ext:field writeonly="true" property="pay.billingReceiveDate"/></td>
	</tr>
	<tr>
		<td>接收人：</td>
		<td><ext:field writeonly="true" property="pay.receiver"/></td>
	</tr>
	<tr>
		<td>是否已付款：</td>
		<td><ext:field writeonly="true" property="pay.isPaid"/></td>
	</tr>
	<tr>
		<td>付款时间：</td>
		<td><ext:field writeonly="true" property="pay.payDate" format="yyyy-MM-dd"/></td>
	</tr>
</table>