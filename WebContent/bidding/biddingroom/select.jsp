<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/openDialog">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/js/selectDialog.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/listdialog/js/listDialog.js"></script>
	<script>
	function initData() {
		
	}
	</script>
<%	request.setAttribute("hideConditionBar", "true");
	request.setAttribute("dataPage", "/bidding/biddingroom/listRooms.jsp"); %>
	<jsp:include page="/jeaf/dialog/selectDialogCommon.jsp" />
</ext:form>