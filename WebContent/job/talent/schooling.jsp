<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveSchooling">
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
			<td nowrap="nowrap" align="right">学校：</td>
			<td><ext:field property="school"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">院系：</td>
			<td><ext:field property="department"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">专业：</td>
			<td><ext:field property="specialty"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">班级：</td>
			<td><ext:field property="schoolClass"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">学历：</td>
			<td><ext:field property="qualification"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">学位：</td>
			<td><ext:field property="degree"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" align="right">专业描述：</td>
			<td><ext:field property="description"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right"></td>
			<td><ext:field property="abroad"/></td>
		</tr>
	</table>
</ext:form>