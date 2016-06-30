<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function publicAgent() {
	if('<ext:write property="biddingAgent.agentName"/>'=='') {
		alert('中选代理不能为空。');
		return;
	}
	FormUtils.doAction('publicAgent');
}
</script>
<div style="width:460px">
	<jsp:include page="../tabpages/biddingAgentRead.jsp" />
</div>