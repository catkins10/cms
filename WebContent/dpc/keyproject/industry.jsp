<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveIndustry">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
		<col width="60px" align="right">
		<col width="100%">
		<tr>
			<td>行业：</td>
			<td><ext:field property="industry.industry"/></td>
		</tr>
		<tr>
			<td>子行业：</td>
			<td><ext:field property="industry.childIndustry"/></td>
		</tr>
	</table>
</ext:form>