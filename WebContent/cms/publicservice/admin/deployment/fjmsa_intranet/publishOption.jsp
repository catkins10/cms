<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" cellspacing="0" cellpadding="3" width="390px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">是否公布：</td>
		<td><ext:field property="publicPass"/></td>
	</tr>
	<ext:notEmpty property="subject">
		<tr>
			<td>公布主题：</td>
			<td><ext:field property="publicSubject"/></td>
		</tr>
	</ext:notEmpty>
	<ext:equal value="false" property="alwaysPublishAll">
		<tr>
			<td>公布正文：</td>
			<td><ext:field property="publicBody"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">公布办理过程：</td>
			<td><ext:field property="publicWorkflow"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td nowrap="nowrap">截止时间：</td>
		<td><ext:field property="publicEnd"/></td>
	</tr>
</table>