<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveEvaluationParameter">
	<table width="100%" border="0" cellpadding="2" cellspacing="0">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">比例：</td>
			<td><ext:field property="ratio"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">最低人数要求：</td>
			<td><ext:field property="minPersonNumber"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">部门领导是否参与：</td>
			<td><ext:field property="leaderEnabled"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">不参与的岗位名称：</td>
			<td><ext:field property="rejectPosts"/></td>
		</tr>
	</table>
</ext:form>