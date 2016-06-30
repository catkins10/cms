<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveMonitorSql">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">采集内容：</td>
			<td>
				<ext:field property="monitorSql.captureContent" onchange="if(value!='' && document.getElementsByName('monitorSql.captureSql')[0].innerText=='')FormUtils.doAction('monitorSql');"/>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top">SQL语句：</td>
			<td><ext:field property="monitorSql.captureSql"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">最后采集时间：</td>
			<td><ext:field property="monitorSql.lastCaptureTime"/></td>
		</tr>
	</table>
</ext:form>