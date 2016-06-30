<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<div style="width:500px">
	填写取回原因:<br>
	<html:textarea property="opinionPackage.opinion" rows="13" style="width:100%;overflow:auto"/>
</div>