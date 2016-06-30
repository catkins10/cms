<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveSuperviseTimeLimit">
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">完成时限：</td>
			<td><ext:field property="superviseTimeLimit.timeLimit"/></td>
		</tr>
	</table>	
</ext:form>