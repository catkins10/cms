<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<ext:equal value="0" property="replyMessageId">
		<tr>
			<td>主题：</td>
			<td><ext:field property="subject"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top">详细说明：</td>
			<td><ext:field property="body"/></td>
		</tr>
	</ext:equal>
	<ext:notEqual value="0" property="replyMessageId">
		<tr>
			<td valign="top">答复：</td>
			<td><ext:field property="body"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td nowrap="nowrap">创建时间：</td>
		<td><ext:field property="created"/></td>
	</tr>
	<tr>
		<td>单位：</td>
		<td><ext:field property="creatorUnit"/></td>
	</tr>
</table>