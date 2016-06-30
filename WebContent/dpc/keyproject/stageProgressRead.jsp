<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">节点名称：</td>
		<td><ext:field writeonly="true" property="stageProgress.stage"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">完成时限：</td>
		<td><ext:field writeonly="true" property="stageProgress.timeLimit"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">责任单位：</td>
		<td><ext:field writeonly="true" property="stageProgress.responsibleUnit"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">责任人：</td>
		<td><ext:field writeonly="true" property="stageProgress.responsiblePerson"/></td>
	</tr>
	<tr>
		<td valign="top" nowrap="nowrap">安排：</td>
		<td><ext:field property="stageProgress.plan" readonly="true"/></td>
	</tr>
	<ext:equal value="1" property="stageProgress.completed">
		<tr>
			<td valign="top" nowrap="nowrap">完成情况：</td>
			<td><ext:field property="stageProgress.progress" readonly="true"/></td>
		</tr>
	</ext:equal>
</table>