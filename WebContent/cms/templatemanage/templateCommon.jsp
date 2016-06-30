<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<link href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet" type="text/css">
<ext:field property="pageHTML"/>
<ext:notEmpty property="errors">
	<script>
		var errors = "";
		<ext:iterate property="errors" id="errorMessage" indexId="errorIndex">
			errors += '<ext:writeNumber name="errorIndex" plus="1"/>、<ext:write name="errorMessage"/>\r\n';
		</ext:iterate>
		window.setTimeout('alert(errors)', 10);
	</script>
</ext:notEmpty>
<ext:notEmpty property="prompt">
	<script>
		window.setTimeout("alert('<ext:write property="prompt"/>')", 10);
	</script>
</ext:notEmpty>