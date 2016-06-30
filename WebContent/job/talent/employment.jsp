<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveEmployment">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">姓名：</td>
			<td width="50%"><ext:field property="name"/></td>
			<td nowrap="nowrap" align="right">毕业年份：</td>
			<td width="50%"><ext:field property="graduationYear"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">专业(班级)：</td>
			<td><ext:field property="schoolClass"/></td>
			<td nowrap="nowrap" align="right">学号：</td>
			<td><ext:field property="studentNumber"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">生源所在地：</td>
			<td colspan="3"><ext:field property="studentSource"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">联系方式：</td>
			<td><ext:field property="tel"/></td>
			<td nowrap="nowrap" align="right">电子邮箱：</td>
			<td><ext:field property="email"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">就业类型：</td>
			<td colspan="3"><ext:field property="employmentType"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">单位名称：</td>
			<td><ext:field property="company"/></td>
			<td nowrap="nowrap" align="right">单位地址：</td>
			<td><ext:field property="companyAddress"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">岗位：</td>
			<td><ext:field property="post"/></td>
			<td nowrap="nowrap" align="right">月薪(元)：</td>
			<td><ext:field property="monthlyPay"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">专业是否对口：</td>
			<td colspan="3"><ext:field property="counterpart"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">就业满意度：</td>
			<td colspan="3"><ext:field property="satisfaction"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">是否更换单位(第几次)：</td>
			<td><ext:field property="changeCompany"/></td>
			<td nowrap="nowrap" align="right">离职原因：</td>
			<td><ext:field property="leaveReason"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">是否需要推荐：</td>
			<td><ext:field property="needHelp"/></td>
			<td nowrap="nowrap" align="right">暂不就业原因：</td>
			<td><ext:field property="waitReason"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">备注：</td>
			<td colspan="3"><ext:field property="remark"/></td>
		</tr>
	</table>
</ext:form>