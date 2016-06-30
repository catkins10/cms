<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/job">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">公司名称：</td>
			<td width="50%"><ext:field writeonly="true" property="company.name"/></td>
			<td nowrap="nowrap" align="right">职位名称：</td>
			<td width="50%"><ext:field writeonly="true" property="name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">招聘部门：</td>
			<td width="50%"><ext:field writeonly="true" property="department"/></td>
			<td nowrap="nowrap" align="right">招聘人数：</td>
			<td width="50%"><ext:field writeonly="true" property="recruitNumber"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right" valign="top">职位描述：</td>
			<td colspan="3"><ext:field writeonly="true" property="description"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right" valign="top">职位要求：</td>
			<td colspan="3"><ext:field writeonly="true" property="requirement"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">工作地点：</td>
			<td width="50%"><ext:field writeonly="true" property="areas.area"/></td>
			<td nowrap="nowrap" align="right">工作性质：</td>
			<td width="50%"><ext:field writeonly="true" property="types.type"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">职能类别：</td>
			<td width="50%"><ext:field writeonly="true" property="post"/></td>
			<td nowrap="nowrap" align="right">语言要求：</td>
			<td width="50%"><ext:field writeonly="true" property="language"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">月薪：</td>
			<td width="50%"><ext:field writeonly="true" property="monthlyPayRange"/></td>
			<td nowrap="nowrap" align="right">招聘对象：</td>
			<td width="50%"><ext:field writeonly="true" property="target"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">学历要求：</td>
			<td width="50%"><ext:field writeonly="true" property="qualification"/></td>
			<td nowrap="nowrap" align="right">工作年限：</td>
			<td width="50%"><ext:field writeonly="true" property="workYear"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">年龄：</td>
			<td width="50%"><ext:field writeonly="true" property="ageRange"/></td>
			<td nowrap="nowrap" align="right">性别：</td>
			<td width="50%"><ext:field writeonly="true" property="sex"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">职称要求：</td>
			<td width="50%"><ext:field writeonly="true" property="rank"/></td>
			<td nowrap="nowrap" align="right">到期时间：</td>
			<td width="50%"><ext:field writeonly="true" property="endDate"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">是否公开：</td>
			<td width="50%"><ext:field writeonly="true" property="isPublic"/></td>
			<td nowrap="nowrap" align="right">是否紧急职位：</td>
			<td width="50%"><ext:field writeonly="true" property="urgent"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">查询次数：</td>
			<td width="50%"><ext:field writeonly="true" property="queryConnt"/></td>
			<td nowrap="nowrap" align="right">投递次数：</td>
			<td width="50%"><ext:field writeonly="true" property="applicantCount"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">创建时间：</td>
			<td width="50%"><ext:field writeonly="true" property="created"/></td>
			<td nowrap="nowrap" align="right">发布时间：</td>
			<td width="50%"><ext:field writeonly="true" property="publicTime"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">刷新时间：</td>
			<td width="50%"><ext:field writeonly="true" property="refreshTime"/></td>
			<td nowrap="nowrap" align="right"></td>
			<td width="50%"></td>
		</tr>
	</table>
</ext:form>