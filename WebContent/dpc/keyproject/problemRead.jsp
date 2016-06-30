<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td>年份：</td>
		<td><ext:field writeonly="true" property="problem.problemYear"/></td>
	</tr>
	<tr>
		<td>月份：</td>
		<td><ext:field writeonly="true" property="problem.problemMonth"/></td>
	</tr>
	<tr>
		<td valign="top">存在的问题：</td>
		<td><ext:field property="problem.problem" readonly="true"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">责任单位或责任人：</td>
		<td><ext:field writeonly="true" property="problem.responsible"/></td>
	</tr>
	<tr>
		<td>解决时限：</td>
		<td><ext:field writeonly="true" property="problem.timeLimit"/></td>
	</tr>
	<ext:equal value="1" property="problem.completed">
		<tr>
			<td valign="top">解决情况：</td>
			<td><ext:field property="problem.result" readonly="true"/></td>
		</tr>
	</ext:equal>
</table>