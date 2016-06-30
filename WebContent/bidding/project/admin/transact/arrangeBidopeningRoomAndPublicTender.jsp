<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function arrangeRoom() {
	FormUtils.doAction('arrangeBidopeningRoomAndPublicTender');
}
</script>
<div style="width:430px">
	<jsp:include page="../tabpages/bidopeningRoomScheduleRead.jsp" />
</div>