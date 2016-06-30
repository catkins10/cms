<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveProject">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">开始时间：</td>
			<td><ext:field property="startDate"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">结束时间：</td>
			<td><ext:field property="endDate"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">项目名称：</td>
			<td><ext:field property="projectName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" align="right">项目描述：</td>
			<td><ext:field property="description"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" align="right">责任描述：</td>
			<td><ext:field property="duty"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right"></td>
			<td><ext:field property="showInResume"/></td>
		</tr>
	</table>
</ext:form>