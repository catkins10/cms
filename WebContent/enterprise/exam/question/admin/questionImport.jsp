<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/doQuestionImport">
	<ext:equal value="create" property="act">
		<table border="0" width="100%" cellspacing="0" cellpadding="3px">
			<col align="right">
			<col width="100%">
			<tr>
				<td nowrap="nowrap">来源：</td>
				<td><ext:field property="source"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">数据文件：</td>
				<td><ext:field property="data"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">说明：</td>
				<td><ext:field property="description"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">适用的岗位：</td>
				<td><ext:field property="posts"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">知识类别：</td>
				<td><ext:field property="knowledges"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">项目分类：</td>
				<td><ext:field property="items"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">备注：</td>
				<td><ext:field property="remark"/></td>
			</tr>
		</table>
	</ext:equal>

	<ext:notEqual value="create" property="act">
		<table border="0" width="100%" cellspacing="0" cellpadding="3px">
			<col align="right">
			<col width="100%">
			<tr>
				<td nowrap="nowrap">来源：</td>
				<td><ext:field writeonly="true" property="source"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">数据文件：</td>
				<td><ext:field writeonly="true" property="data"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">说明：</td>
				<td><ext:field writeonly="true" property="description"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">适用的岗位：</td>
				<td><ext:field writeonly="true" property="posts"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">知识类别：</td>
				<td><ext:field writeonly="true" property="knowledges"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">项目分类：</td>
				<td><ext:field writeonly="true" property="items"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">导入时间：</td>
				<td><ext:field writeonly="true" property="created"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">操作者：</td>
				<td><ext:field writeonly="true" property="creator"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap">备注：</td>
				<td><ext:field writeonly="true" property="remark"/></td>
			</tr>
		</table>
	</ext:notEqual>
</ext:form>