<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<col>
	<col width="100%">
	<tr>
		<td nowrap="nowrap" align="right">时间：</td>
		<td><ext:field property="discuss.discussTime"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">地点：</td>
		<td><ext:field property="discuss.discussAddress"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">洽谈人：</td>
		<td><ext:field property="discuss.discussPerson"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right" valign="top">洽谈内容：</td>
		<td><ext:field property="discuss.discussContent"/></td>
	</tr>
</table>