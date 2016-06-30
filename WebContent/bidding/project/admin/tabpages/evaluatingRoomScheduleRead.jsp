<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">评标室</td>
		<td class="tdcontent"><ext:field writeonly="true" property="evaluatingRoomSchedule.roomName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开始使用时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="evaluatingRoomSchedule.beginTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">结束使用时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="evaluatingRoomSchedule.endTime"/></td>
	</tr>
</table>