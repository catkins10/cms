<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveUnitCode">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">机构：</td>
			<td><ext:field property="unitCode.unitName"/></td>
		</tr>
		<tr>
			<td>代码：</td>
			<td><ext:field property="unitCode.code"/></td>
		</tr>
	</table>
</ext:form>