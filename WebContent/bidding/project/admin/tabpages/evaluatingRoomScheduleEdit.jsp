<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">开标时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="bidopening.beginTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开始使用时间</td>
		<td class="tdcontent"><ext:field property="evaluatingRoomSchedule.beginTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">结束使用时间</td>
		<td class="tdcontent"><ext:field property="evaluatingRoomSchedule.endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">分配评标室</td>
		<td class="tdcontent"><ext:field property="evaluatingRoomSchedule.roomName"/></td>
	</tr>
</table>
<html:hidden property="evaluatingRoomSchedule.roomType" value="评标"/>