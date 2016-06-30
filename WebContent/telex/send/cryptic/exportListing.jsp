<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doExportListing" target="_blank">
   	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">年度：</td>
			<td><ext:field property="year"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">月份：</td>
			<td><ext:field property="month"/></td>
		</tr>
	</table>
</ext:form>