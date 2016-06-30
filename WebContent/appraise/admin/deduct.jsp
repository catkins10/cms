<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveDeduct">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">单位名称：</td>
			<td><ext:field property="unitName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">评议年度：</td>
			<td><ext:field property="year"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">扣分：</td>
			<td><ext:field property="deduct"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">扣分原因：</td>
			<td><ext:field property="reason"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">登记人：</td>
			<td><ext:field property="creator"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">登记时间：</td>
			<td><ext:field property="created"/></td>
		</tr>
	</table>
</ext:form>