<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="100px" align="right">
	<col width="100%">
	<tr>
		<td>总投资(万元)：</td>
		<td><ext:write property="projectInvest.amount"/></td>
	</tr>
	<tr>
		<td>资金来源：</td>
		<td>
			<ext:write property="projectInvest.fullSource"/>
		</td>
	</tr>
	<tr>
		<td>来源说明：</td>
		<td><ext:write property="projectInvest.remark"/></td>
	</tr>
</table>