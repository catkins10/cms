<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function arrangeRoom() {
	if('<ext:write property="evaluatingRoomSchedule.roomName"/>'=='') {
		alert('评标室不能为空。');
		return;
	}
	if('<ext:write property="evaluatingRoomSchedule.beginTime"/>'=='') {
		alert('开始使用时间不能为空。');
		return;
	}
	if('<ext:write property="evaluatingRoomSchedule.endTime"/>'=='') {
		alert('结束使用时间不能为空。');
		return;
	}
	var beginTime = new Date('<ext:write property="bidopeningRoomSchedule.beginTime" format="yyyy/MM/dd HH:mm:ss"/>');
	var endTime = new Date('<ext:write property="bidopeningRoomSchedule.endTime" format="yyyy/MM/dd HH:mm:ss"/>');
	var diff = endTime - beginTime;
	if(diff<=0) {
		alert('开始使用时间不能早于结束使用时间。');
		return;
	}
	if(diff > 24*3600*1000) {
		alert("结束时间超过开始时间24小时");
		return;
	}
	if(beginTime.getHours()<7) {
		alert("开始使用时间不正确");
		return;
	}
	FormUtils.doAction('arrangeEvaluatingRoom');
}
</script>
<div style="width:430px">
	<jsp:include page="../tabpages/evaluatingRoomScheduleRead.jsp" />
</div>