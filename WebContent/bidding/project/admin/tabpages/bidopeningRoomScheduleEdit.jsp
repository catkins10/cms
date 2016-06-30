<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">开标时间</td>
		<td class="tdcontent">
			<ext:field writeonly="true" property="plan.bidopeningTime"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开始使用时间</td>
		<td class="tdcontent"><ext:field property="bidopeningRoomSchedule.beginTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">结束使用时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td nowrap="nowrap">
						<input onclick="setEndTime()" type="radio" class="radio" name="bidopeningRoomAmPm" id="bidopeningRoomAm">&nbsp;<label for="bidopeningRoomAm">上午</label>
						<input onclick="setEndTime()" type="radio" class="radio" name="bidopeningRoomAmPm" id="bidopeningRoomPm">&nbsp;<label for="bidopeningRoomPm">下午</label>&nbsp;&nbsp;
					</td>
					<td><ext:field property="bidopeningRoomSchedule.endTime"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">分配开标室</td>
		<td class="tdcontent"><ext:field property="bidopeningRoomSchedule.roomName"/></td>
	</tr>
</table>
<html:hidden property="bidopeningRoomSchedule.roomType" value="开标"/>
<script>
	var workEndAmHour = Number('<ext:write property="workEndAm"/>'.split(':')[0]);
	var workEndAmMinute = Number('<ext:write property="workEndAm"/>'.split(':')[1]);
	var workEndPmHour = Number('<ext:write property="workEndPm"/>'.split(':')[0]);
	var workEndPmMinute = Number('<ext:write property="workEndPm"/>'.split(':')[1]);
	function setEndTime() {
		var am = document.getElementsByName('bidopeningRoomAmPm')[0].checked;
		DateTimeField.setValue('bidopeningRoomSchedule.endTime', 'hour', (am ? workEndAmHour : workEndPmHour));
		DateTimeField.setValue('bidopeningRoomSchedule.endTime', 'minute', (am ? workEndAmMinute : workEndPmMinute));
	}
	if(workEndAmHour==Number('<ext:write property="bidopeningRoomSchedule.endTime" format="H"/>') && workEndAmMinute==Number('<ext:write property="bidopeningRoomSchedule.endTime" format="m"/>')) {
		document.getElementsByName('bidopeningRoomAmPm')[0].checked = true;
	}
	else if(workEndPmHour==Number('<ext:write property="bidopeningRoomSchedule.endTime" format="H"/>') && workEndPmMinute==Number('<ext:write property="bidopeningRoomSchedule.endTime" format="m"/>')) {
		document.getElementsByName('bidopeningRoomAmPm')[1].checked = true;
	}
</script>