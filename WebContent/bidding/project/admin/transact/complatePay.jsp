<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function completePay() {
	if(<ext:write property="pay.tendereeMoney"/>==0) {
		alert('建设单位缴费金额未设置。');
		return;
	}
	if(<ext:write property="pay.pitchonMoney"/>==0) {
		alert('中标单位缴费金额未设置。');
		return;
	}
	FormUtils.doAction('completePay');
}
</script>
<div style="width:460px">
	<jsp:include page="../tabpages/payRead.jsp" />
</div>