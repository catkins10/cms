<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<ext:form applicationName="jeaf/usermanage" pageName="member">
	<div class="prompt"><ext:write property="actionResult"/></div>
</ext:form>