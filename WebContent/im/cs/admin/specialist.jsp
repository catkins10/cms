<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveSpecialist">
   	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col valign="middle" align="right">
		<col valign="middle" width="100%">
		<tr>
			<td nowrap="nowrap">客服人员:</td>
			<td><ext:field property="personName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">对外显示的用户名:</td>
			<td><ext:field property="externalName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">最大并发对话数量:</td>
			<td><ext:field property="maxChat"/></td>
		</tr>
	</table>
</ext:form>