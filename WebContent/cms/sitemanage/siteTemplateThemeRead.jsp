<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="5px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">主题所属站点：</td>
		<td><ext:field writeonly="true" property="siteName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">主题名称：</td>
		<td><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">浏览器类型：</td>
		<td><ext:field writeonly="true" property="typeAsText"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">当前站点：</td>
		<td><ext:field writeonly="true" property="pageSiteName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">是否默认主题：</td>
		<td><ext:equal value="true" property="defaultTheme">是</ext:equal></td>
	</tr>
</table>