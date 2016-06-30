<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doInitSystem">
	<table width="360px" border="0" cellpadding="3" cellspacing="0">
		<col valign="middle">
		<col valign="middle" width="100%">
		<tr>
			<td align="right">系统名称：</td>
			<td><html:text styleClass="field" property="systemName"/></td>
		</tr>
		<tr>
			<td align="right">管理员姓名：</td>
			<td><html:text styleClass="field" property="managerName"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">管理员登录名：</td>
			<td><html:text styleClass="field" property="managerLoginName"/></td>
		</tr>
		<tr>
			<td align="right">管理员密码：</td>
			<td><html:text styleClass="field" property="managerPassword"/></td>
		</tr>
	</table>
</ext:form>