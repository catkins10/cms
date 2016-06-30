<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="150px" align="right">
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
		<td valign="top">存在的问题及工作建议：</td>
		<td><ext:field property="problem.problem" readonly="true"/></td>
	</tr>
</table>