<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<ext:notEmpty property="errors">
	<script>
		var syetemErrors = '<ext:iterate property="errors" id="errorMessage" indexId="errorIndex"><ext:sizeNotEqual property="errors" value="1"><ext:writeNumber name="errorIndex" plus="1"/>、</ext:sizeNotEqual><ext:write name="errorMessage"/>\r\n</ext:iterate>';
		window.setTimeout('alert(syetemErrors)', 100);
    </script>
</ext:notEmpty>
<ext:notEmpty property="prompt">
	<script>
		var syetemPrompt = '<ext:write property="prompt"/>';
		window.setTimeout('alert(syetemPrompt)', 100);
	</script>
</ext:notEmpty>
<ext:notEmpty property="actionResult">
	<script>
		var syetemPrompt = '<ext:write property="actionResult"/>';
		window.setTimeout('alert(syetemPrompt)', 100);
	</script>
</ext:notEmpty>