<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newCommuteTime() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/timetable/commuteTime.shtml?id=<ext:write property="id"/>', 550, 360);
	}
	function openCommuteTime(commuteTimeId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/timetable/commuteTime.shtml?id=<ext:write property="id"/>&commuteTime.id=' + commuteTimeId, 550, 360);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加上下班时间" style="width:120px" onclick="newCommuteTime()">
	</div>
<%}%>

<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td nowrap="nowrap" class="tdtitle" width="36px">序号</td>
		<td nowrap="nowrap" class="tdtitle" width="100%">描述</td>
		<td nowrap="nowrap" class="tdtitle" width="130px">启用时间</td>
		<td nowrap="nowrap" class="tdtitle" width="100px">上班时间</td>
		<td nowrap="nowrap" class="tdtitle" width="100px">下班时间</td>
		<td nowrap="nowrap" class="tdtitle" width="130px">上班打卡时间</td>
		<td nowrap="nowrap" class="tdtitle" width="130px">下班打卡时间</td>
		<td nowrap="nowrap" class="tdtitle" width="80px">迟到时长</td>
		<td nowrap="nowrap" class="tdtitle" width="80px">早退时长</td>
	</tr>
	<ext:iterate id="commuteTime" indexId="commuteTimeIndex" property="commuteTimes">
		<tr onclick="openCommuteTime('<ext:write name="commuteTime" property="id"/>')" align="center">
			<td class="tdcontent" align="center"><ext:writeNumber name="commuteTimeIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="commuteTime" property="description"/></td>
			<td class="tdcontent">
				<ext:write name="commuteTime" property="effectiveBegin"/>
				<ext:notEmpty name="commuteTime" property="effectiveBegin"><ext:notEmpty name="commuteTime" property="effectiveEnd"> ~ </ext:notEmpty></ext:notEmpty>
				<ext:write name="commuteTime" property="effectiveEnd"/>
			</td>
			<td class="tdcontent"><ext:write name="commuteTime" property="onDutyTime"/></td>
			<td class="tdcontent"><ext:write name="commuteTime" property="offDutyTime"/></td>
			<td class="tdcontent"><ext:write name="commuteTime" property="clockInBegin"/> ~ <ext:write name="commuteTime" property="clockInEnd"/></td>
			<td class="tdcontent"><ext:write name="commuteTime" property="clockOutBegin"/> ~ <ext:write name="commuteTime" property="clockOutEnd"/></td>
			<td class="tdcontent"><ext:write name="commuteTime" property="lateMiniutes"/>分钟</td>
			<td class="tdcontent"><ext:write name="commuteTime" property="earlyMiniutes"/>分钟</td>
		</tr>
	</ext:iterate>
</table>