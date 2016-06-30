<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doRefreshAllJobs">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">剩余刷新次数：</td>
			<td><ext:field property="refreshTimesLeft"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">最后刷新时间：</td>
			<td><ext:field property="lastRefreshTime"/></td>
		</tr>
	</table>
</ext:form>