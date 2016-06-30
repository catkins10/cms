<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<ext:notEmpty property="errors">
	<br>
	<table border="0" cellspacing="0" cellpadding="0">
		<tr> 
			<td valign="top"><img src="<%=request.getContextPath()%>/jeaf/form/img/warn.gif">&nbsp;</td>
			<td style="color:red">
				<ext:iterate property="errors" id="errorMessage" indexId="errorIndex">
					<pre style="margin:0;word-wrap:break-word"><ext:sizeNotEqual value="1" property="errors"><ext:writeNumber name="errorIndex" plus="1"/>、</ext:sizeNotEqual><ext:write name="errorMessage"/></pre>
				</ext:iterate>
    		</td>
		</tr>
	</table>
</ext:notEmpty>
<ext:notEmpty property="prompt">
	<br>
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top"><img src="<%=request.getContextPath()%>/jeaf/form/img/warn.gif">&nbsp;</td>
			<td style="color:red"><ext:write property="prompt"/></td>
		</tr>
	</table>
</ext:notEmpty>
<ext:notEmpty property="actionResult">
	<br>
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top"><img src="<%=request.getContextPath()%>/jeaf/form/img/warn.gif">&nbsp;</td>
			<td style="color:red"><ext:write property="actionResult"/></td>
		</tr>
	</table>
</ext:notEmpty>