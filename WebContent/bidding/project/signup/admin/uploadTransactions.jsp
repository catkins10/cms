<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/processTransactions">
   	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">银行：</td>
			<td><ext:field property="paymentMethod"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">交易记录：</td>
			<td><ext:field property="attachment"/></td>
		</tr>
	</table>
</ext:form>