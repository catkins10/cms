<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<%request.setAttribute("writeFormPrompt", "true");%>
<ext:form applicationName="bidding/project/ask" pageName="biddingAsk">
	<ext:write property="actionResult"/>
</ext:form>