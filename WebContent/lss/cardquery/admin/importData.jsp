<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/doImportData">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="left">
		<col width="100%">
		<tr>
			<td nowrap="nowrap" colspan="2">请确保相关的制卡类型参数已配置完成，否则导入将不成功</td>
		</tr>
		<tr>
			<td nowrap="nowrap">数据文件：</td>
			<td><ext:field property="data"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">数据日期：</td>
			<td><ext:field property="mark"/></td>
		</tr>
	</table>
</ext:form>