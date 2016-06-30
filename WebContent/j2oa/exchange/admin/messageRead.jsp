<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td>公文主题：</td>
		<td><ext:field writeonly="true" property="document.subject"/></td>
	</tr>
	<tr>
		<td>反馈主题：</td>
		<td><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">详细说明：</td>
		<td><ext:field writeonly="true" readonly="true" property="body"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">创建时间：</td>
		<td><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td>单位：</td>
		<td><ext:field property="creatorUnit"/></td>
	</tr>
	<ext:notEmpty property="reply">
		<tr>
			<td valign="top">答复：</td>
			<td><ext:field writeonly="true" property="reply.body"/></td>
		</tr>
		<tr>
			<td>答复时间：</td>
			<td><ext:field writeonly="true" property="reply.created"/></td>
		</tr>
	</ext:notEmpty>
</table>