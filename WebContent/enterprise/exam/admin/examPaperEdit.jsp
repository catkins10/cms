<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">考卷名称：</td>
		<td><ext:field property="examPaperName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">考试时间：</td>
		<td>
			<table border="0" width="100%" cellspacing="0" cellpadding="0">
				<tr>
					<td width="50%"><ext:field property="beginTime"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td width="50%"><ext:field property="endTime"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap">计算机作答：</td>
		<td><ext:field property="onComputer"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">参加考试人员：</td>
		<td><ext:field property="testUsers.visitorNames"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">出卷时间：</td>
		<td><ext:field property="created"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">出卷人：</td>
		<td><ext:field property="creator"/></td>
	</tr>
	<ext:notEmpty property="releaseTime">
		<tr>
			<td nowrap="nowrap">发布时间：</td>
			<td><ext:field property="releaseTime"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">发布人：</td>
			<td><ext:field property="releasePerson"/></td>
		</tr>
	</ext:notEmpty>
</table>