<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveAssessStandard">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
		<col width="130px" align="right">
		<col width="100%">
		<tr>
			<td>序号：</td>
			<td><ext:field property="standard.priority"/></td>
		</tr>
		<tr>
			<td valign="top">考核内容：</td>
			<td><ext:field property="standard.content"/></td>
		</tr>
		<tr>
			<td valign="top">考核说明及评分标准：</td>
			<td><ext:field property="standard.standard"/></td>
		</tr>
		<tr>
			<td>最大分值：</td>
			<td><ext:field property="standard.maxValue"/></td>
		</tr>
	</table>
</ext:form>