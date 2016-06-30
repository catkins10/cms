<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveTraining">
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
			<td nowrap="nowrap" align="right">培训机构：</td>
			<td><ext:field property="organization"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">培训地点：</td>
			<td><ext:field property="address"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">培训课程：</td>
			<td><ext:field property="course"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">获得证书：</td>
			<td><ext:field property="certificate"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" align="right">详细描述：</td>
			<td><ext:field property="description"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right"></td>
			<td><ext:field property="showInResume"/></td>
		</tr>
	</table>
</ext:form>