<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<ext:notEmpty name="sign" property="signTelegrams">
	<jsp:include page="printSignBill.jsp" />
</ext:notEmpty>
<ext:notEmpty name="sign" property="returnTelegrams">
	<jsp:include page="printReturnBill.jsp" />
</ext:notEmpty>