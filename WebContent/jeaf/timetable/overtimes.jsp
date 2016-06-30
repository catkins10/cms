<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newOvertime() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/timetable/overtime.shtml?id=<ext:write property="id"/>', 430, 250);
	}
	function openOvertime(overtimeId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/timetable/overtime.shtml?id=<ext:write property="id"/>&overtime.id=' + overtimeId, 430, 250);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加配置" style="width:100px" onclick="newOvertime()">
	</div>
<%}%>
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td nowrap="nowrap" class="tdtitle" width="80px">加班小时数</td>
		<td nowrap="nowrap" class="tdtitle">换算的加班天数</td>
	</tr>
	<ext:iterate id="overtime" indexId="overtimeIndex" property="overtimes">
		<tr onclick="openOvertime('<ext:write name="overtime" property="id"/>')" align="left">
			<td class="tdcontent" align="center"><ext:write name="overtime" property="onDutyTime" format="#.##"/></td>
			<td class="tdcontent"><ext:write name="overtime" property="absentDay" format="#.##"/></td>
		</tr>
	</ext:iterate>
</table>