<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/completePunchCard">
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000" width="100%">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap>用户名：</td>
			<td><ext:write name="SessionInfo" property="userName" scope="session"/></td>
		</tr>
		<tr>
			<td>时　间：</td>
			<td><ext:write property="punchTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</table>
</ext:form>