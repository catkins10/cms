<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function completeUseFee() {
	if(<ext:write property="useFee.money"/>==0 && !confirm("缴费金额为0元，是否确定？")) {
		return;
	}
	FormUtils.doAction('completeUseFee');
}
</script>
<div style="width:400px">
	<jsp:include page="../tabpages/useFeeRead.jsp" />
</div>