<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">领导姓名：</td>
		<td><ext:field writeonly="true" property="instruct.leader"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">领导级别：</td>
		<td><ext:field writeonly="true" property="instruct.level"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">批示内容：</td>
		<td><ext:field writeonly="true" property="instruct.instruct"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">批示时间：</td>
		<td><ext:field writeonly="true" property="instruct.instructTime"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">录入人：</td>
		<td><ext:field writeonly="true" property="instruct.creator"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">录入时间：</td>
		<td><ext:field writeonly="true" property="instruct.created"/></td>
	</tr>
</table>