<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/doImportData">
	<ext:equal value="create" property="act">
		<table border="0" width="100%" cellspacing="0" cellpadding="3px">
			<col align="right">
			<col width="100%">
			<tr>
				<td nowrap="nowrap">数据文件：</td>
				<td><ext:field property="data"/></td>
			</tr>
		</table>
	</ext:equal>
	<ext:notEqual value="create" property="act">
		<table border="0" width="100%" cellspacing="0" cellpadding="3px">
			<col align="right">
			<col width="100%">
			<tr>
				<td nowrap="nowrap">数据文件：</td>
				<td><ext:field writeonly="true" property="data"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">导入时间：</td>
				<td><ext:field writeonly="true" property="created"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap" valign="top">操作者：</td>
				<td><ext:field writeonly="true" property="creator"/></td>
			</tr>
		</table>
	</ext:notEqual>
</ext:form>