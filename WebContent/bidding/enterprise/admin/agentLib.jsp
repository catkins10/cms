<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveAgentLib">
   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">代理名录库</td>
			<td class="tdcontent"><ext:field property="lib"/></td>
		</tr>
	</table>
</ext:form>