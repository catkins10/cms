<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<div style="width:360px">
	<br>
	<b>&nbsp;&nbsp;<ext:write name="workflowPrompt"/></b>
	<br><br>
</div>