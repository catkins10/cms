<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="500px" border="0" cellpadding="3" cellspacing="0">
	<tr>
		<td nowrap="nowrap">编号</td>
		<td><ext:field property="feedbackNumber" /></td>
	</tr>
	<tr>
		<td nowrap="nowrap">送达人</td>
		<td><ext:field property="feedbackSender" /></td>
	</tr>
	<tr>
		<td nowrap="nowrap">送达时间</td>
		<td><ext:field property="feedbackSendTime" /></td>
	</tr>
	<tr>
		<td nowrap="nowrap">办理评价</td>
		<td><ext:field property="appraise" /></td>
	</tr>
	<tr>
		<td nowrap="nowrap">评价人</td>
		<td><ext:field property="appraiser" /></td>
	</tr>
	<tr>
		<td nowrap="nowrap">评价时间</td>
		<td><ext:field property="appraiseTime" /></td>
	</tr>
	<tr>
		<td nowrap="nowrap">联系方式</td>
		<td><ext:field property="appraiserTel" /></td>
	</tr>
</table>