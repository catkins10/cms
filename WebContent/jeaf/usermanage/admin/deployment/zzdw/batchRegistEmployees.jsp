<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/doBatchRegistEmployees">
   	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">单位/部门：</td>
			<td><ext:field writeonly="true" property="orgFullName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">用户清单：</td>
			<td>
				<ext:field property="data"/>
				<div style="padding-top: 5px">
					<a target="_blank" href="<%=request.getContextPath()%>/jeaf/usermanage/template/用户导入.xls">模板下载</a>
				</div>
			</td>
		</tr>
	</table>
</ext:form>