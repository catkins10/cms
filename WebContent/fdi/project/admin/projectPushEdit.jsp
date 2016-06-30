<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<col>
	<col width="100%">
	<tr>
		<td nowrap="nowrap" align="right">时间：</td>
		<td><ext:field property="push.pushTime"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">经办人：</td>
		<td><ext:field property="push.transactor"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right" valign="top">洽谈内容：</td>
		<td><ext:field property="push.content"/></td>
	</tr>
</table>