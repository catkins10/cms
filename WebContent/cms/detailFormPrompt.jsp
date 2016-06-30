<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:notEmpty property="errors">
	<script>
		alert('<ext:iterate property="errors" id="errorMessage" indexId="errorIndex"><ext:writeNumber name="errorIndex" plus="1"/>、<ext:write name="errorMessage"/>\r\n</ext:iterate>');
    </script>
</ext:notEmpty>
<ext:notEmpty property="prompt">
	<script>
		alert('<ext:write property="prompt"/>');
	</script>
</ext:notEmpty>