<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td>时间：</td>
		<td><ext:field property="rewardsPunishment.happenDate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">奖惩内容：</td>
		<td><ext:field property="rewardsPunishment.content"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">奖惩原因：</td>
		<td><ext:field property="rewardsPunishment.reason"/></td>
	</tr>
	<tr>
		<td>备注：</td>
		<td><ext:field property="rewardsPunishment.remark"/></td>
	</tr>
</table>