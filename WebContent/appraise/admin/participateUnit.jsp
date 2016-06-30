<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveParticipateUnit">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">地区：</td>
			<td><ext:field property="area"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">年度：</td>
			<td><ext:field property="year"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">单位分类：</td>
			<td><ext:field property="category"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">参评单位：</td>
			<td><ext:field property="unitNames"/></td>
		</tr>
	</table>
</ext:form>