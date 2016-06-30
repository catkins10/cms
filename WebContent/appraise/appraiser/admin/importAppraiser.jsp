<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/doImportAppraiser">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">区域：</td>
			<td><ext:field property="orgName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">有效期：</td>
			<td><ext:field property="expire"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">数据文件：</td>
			<td><ext:field property="data"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap"></td>
			<td><a target="_blank" href="<%=request.getContextPath()%>/appraise/appraiser/template/基础库评议员.xls">模板下载</a></td>
		</tr>
	</table>
</ext:form>