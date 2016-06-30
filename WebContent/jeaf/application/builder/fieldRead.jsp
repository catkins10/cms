<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">名称：</td>
		<td><ext:field writeonly="true" property="field.name"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">英文名称：</td>
		<td><ext:field writeonly="true" property="field.englishName"/></td>
	</tr>
</table>