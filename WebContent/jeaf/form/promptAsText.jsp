<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<ext:notEmpty property="errors">
	<ext:iterate property="errors" id="errorMessage" indexId="errorIndex">
		<pre style="margin:0;word-wrap:break-word"><ext:sizeNotEqual property="errors" value="1"><ext:writeNumber name="errorIndex" plus="1"/>、</ext:sizeNotEqual><ext:write name="errorMessage"/></pre>
	</ext:iterate>
 </ext:notEmpty>
<ext:notEmpty property="prompt">
	<ext:write property="prompt"/>
</ext:notEmpty>
<ext:notEmpty property="actionResult">
	<ext:write property="actionResult"/>
</ext:notEmpty>