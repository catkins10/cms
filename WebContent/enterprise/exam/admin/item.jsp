<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveItem">
	<table width="100%" border="0" cellpadding="2" cellspacing="0">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">项目分类：</td>
			<td><ext:field property="item"/></td>
		</tr>
	</table>
</ext:form>