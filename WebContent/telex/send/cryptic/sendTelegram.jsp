<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveSendTelegram">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/telex/js/telex.js"></script>
   	<ext:subForm/>
</ext:form>