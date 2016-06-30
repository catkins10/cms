<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveBlacklist">
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">公司名称/个人姓名：</td>
			<td><ext:field property="userName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">加入黑名单时间：</td>
			<td><ext:field property="blacklistBegin"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">从黑名单删除时间：</td>
			<td><ext:field property="blacklistEnd"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">加入原因：</td>
			<td><ext:field property="reason"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">备注：</td>
			<td><ext:field property="remark"/></td>
		</tr>
	</table>
</ext:form>