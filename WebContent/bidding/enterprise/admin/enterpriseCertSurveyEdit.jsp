<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<tr>
		<td align="right" width="70px">年检时间：</td>
		<td><ext:field property="survey.surveyDate"/></td>
	</tr>
	<tr>
		<td align="right">年份：</td>
		<td><ext:field property="survey.surveyYear"/></td>
	</tr>
	<tr>
		<td align="right">年检情况：</td>
		<td><ext:field property="survey.surveyResult"/></td>
	</tr>
	<tr>
		<td align="right">备注：</td>
		<td><ext:field property="survey.remark"/></td>
	</tr>
</table>