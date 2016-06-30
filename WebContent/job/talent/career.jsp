<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveCareer">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">开始时间：</td>
			<td width="50%"><ext:field property="startDate"/></td>
			<td nowrap="nowrap" align="right">结束时间：</td>
			<td width="50%"><ext:field property="endDate"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">公司：</td>
			<td><ext:field property="company"/></td>
			<td nowrap="nowrap" align="right">行业：</td>
			<td><ext:field property="industry"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">公司规模：</td>
			<td><ext:field property="scale"/></td>
			<td nowrap="nowrap" align="right">公司性质：</td>
			<td><ext:field property="type"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" align="right">所在地：</td>
			<td><ext:field property="area"/></td>
			<td nowrap="nowrap" valign="top" align="right">部门：</td>
			<td><ext:field property="department"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">职位：</td>
			<td><ext:field property="job"/></td>
			<td nowrap="nowrap" align="right">职位类别：</td>
			<td><ext:field property="post"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right" valign="top">工作描述：</td>
			<td colspan="3"><ext:field property="description"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">月薪(元)：</td>
			<td><ext:field property="monthlyPay"/></td>
			<td nowrap="nowrap" align="right">证明人：</td>
			<td><ext:field property="reterence"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">证明人职务：</td>
			<td><ext:field property="reterenceJob"/></td>
			<td nowrap="nowrap" align="right">证明人电话：</td>
			<td><ext:field property="reterenceTel"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right" valign="top">离职原因：</td>
			<td colspan="3"><ext:field property="leaveReason"/></td>
		</tr>
	</table>
</ext:form>