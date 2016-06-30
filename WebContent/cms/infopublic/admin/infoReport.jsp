<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/writeInfoReport" target="_blank" method="get">
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000" width="100%">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap>开始时间：</td>
			<td><ext:field property="beginDate"/></td>
		</tr>
		<tr>
			<td>结束时间：</td>
			<td><ext:field property="endDate"/></td>
		</tr>
	</table>
</ext:form>