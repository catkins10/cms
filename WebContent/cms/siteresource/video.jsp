<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveVideo">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">视频：</td>
			<td><ext:field property="video.name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">标题：</td>
			<td><ext:field property="video.subject"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top">描述：</td>
			<td><ext:field property="video.description"/></td>
		</tr>
	</table>
</ext:form>