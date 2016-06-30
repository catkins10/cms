<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveLeaderAgent">
   	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">领导：</td>
			<td><ext:field property="leader"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">代理人：</td>
			<td><ext:field property="agent"/></td>
		</tr>
		<tr class="readonlyrow">
			<td nowrap="nowrap">创建人：</td>
			<td><ext:field property="creator"/></td>
		</tr>
		<tr class="readonlyrow">
			<td nowrap="nowrap">创建时间：</td>
			<td><ext:field property="created"/></td>
		</tr>
	</table>
</ext:form>