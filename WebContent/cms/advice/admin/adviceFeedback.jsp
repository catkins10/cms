<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveAdviceFeedback">
   	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col valign="middle" width="100%">
		<tr>
			<td><ext:field property="adviceFeedback.feedback"/></td>
		</tr>
	</table>
</ext:form>