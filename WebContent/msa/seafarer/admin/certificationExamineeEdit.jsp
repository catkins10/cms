<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="0" cellpadding="3" cellspacing="0">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">准考证号码：</td>
		<td><ext:field property="examinee.permit"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">姓名：</td>
		<td><ext:field property="examinee.name"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">申考等级：</td>
		<td><ext:field property="examinee.level"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">申考职务：</td>
		<td><ext:field property="examinee.job"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">考试结果：</td>
		<td><ext:field property="examinee.examResult"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">评估结果：</td>
		<td><ext:field property="examinee.result"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">合格证明序列号：</td>
		<td><ext:field property="examinee.sn"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">发放日期：</td>
		<td><ext:field property="examinee.grantDate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">领取日期：</td>
		<td><ext:field property="examinee.receiveDate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">身份证号码：</td>
		<td><ext:field property="examinee.identityCard"/></td>
	</tr>
</table>