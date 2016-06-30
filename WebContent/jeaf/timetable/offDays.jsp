<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newOffDay() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/timetable/offDay.shtml?id=<ext:write property="id"/>', 430, 250);
	}
	function openOffDay(offDayId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/timetable/offDay.shtml?id=<ext:write property="id"/>&offDay.id=' + offDayId, 430, 250);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加日常休息时间" style="width:120px" onclick="newOffDay()">
	</div>
<%}%>
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td nowrap="nowrap" class="tdtitle">休息时间</td>
	</tr>
	<ext:iterate id="offDay" indexId="offDayIndex" property="offDays">
		<tr onclick="openOffDay('<ext:write name="offDay" property="id"/>')" align="left">
			<td class="tdcontent">
				<ext:write name="offDay" property="offDayNameOfWeek"/>&nbsp;
				<ext:write name="offDay" property="beginTime"/>
				~
				<ext:write name="offDay" property="endTime"/>
			</td>
		</tr>
	</ext:iterate>
</table>