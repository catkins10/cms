<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveAbility">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">名称：</td>
			<td width="100%"><ext:field property="name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" align="right">描述：</td>
			<td><ext:field property="description"/></td>
		</tr>
	</table>
</ext:form>