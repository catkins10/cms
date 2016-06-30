<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveOpinion">
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">意见类型：</td>
			<td><ext:field property="opinionType"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">填 写 人：</td>
			<td><ext:field property="personName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">填写时间：</td>
			<td><ext:field property="created"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top">意见内容：</td>
			<td><ext:field property="opinion"/></td>
		</tr>
	</table>
</ext:form>