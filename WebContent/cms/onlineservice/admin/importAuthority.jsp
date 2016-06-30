<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/doImportAuthority">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">上传数据文件：</td>
			<td><ext:field property="files"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">把SHEET作为目录：</td>
			<td><ext:field property="sheetAsDirectory"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">导入到其他目录：</td>
			<td><ext:field property="otherDirectoryName"/></td>
		</tr>
	</table>
</ext:form>