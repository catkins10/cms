<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveCertificate">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">获得时间：</td>
			<td><ext:field property="gained"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">证书名称：</td>
			<td><ext:field property="certificateName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">成绩：</td>
			<td><ext:field property="mark"/></td>
		</tr>
	</table>
</ext:form>