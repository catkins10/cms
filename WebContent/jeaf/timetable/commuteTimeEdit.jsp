<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="120px" align="right">
	<col width="100%">
	<tr title="如:上午上班">
		<td>描述：</td>
		<td><ext:field property="commuteTime.description"/></td>
	</tr>
	<tr title="如果有夏令时,则需要设置">
		<td>启用时间：</td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><ext:field property="commuteTime.effectiveBegin"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td><ext:field property="commuteTime.effectiveEnd"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>加班时间：</td>
		<td><ext:field property="commuteTime.isOvertime"/></td>
	</tr>
	<tr>
		<td>上班时间：</td>
		<td><ext:field property="commuteTime.onDutyTime"/></td>
	</tr>
	<tr>
		<td>上班打卡时间：</td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><ext:field property="commuteTime.clockInBegin"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td><ext:field property="commuteTime.clockInEnd"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>下班时间：</td>
		<td><ext:field property="commuteTime.offDutyTime"/></td>
	</tr>
	<tr>
		<td>下班打卡时间：</td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><ext:field property="commuteTime.clockOutBegin"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td><ext:field property="commuteTime.clockOutEnd"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr title="迟到多长时间算缺勤,以分钟为单位">
		<td>迟到时长(分钟)：</td>
		<td><ext:field property="commuteTime.lateMiniutes"/></td>
	</tr>
	<tr title="早退多长时间算缺勤,以分钟为单位">
		<td>早退时长(分钟)：</td>
		<td><ext:field property="commuteTime.earlyMiniutes"/></td>
	</tr>
	<tr>
		<td>上班天数换算：</td>
		<td><ext:field property="commuteTime.absentDay"/></td>
	</tr>
</table>