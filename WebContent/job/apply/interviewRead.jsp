<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">面试官：</td>
		<td><ext:field writeonly="true" property="interview.interviewer"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">面试时间：</td>
		<td><ext:field writeonly="true" property="interview.interviewTime"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">面试情况：</td>
		<td><ext:field writeonly="true" property="interview.description"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">面试意见：</td>
		<td><ext:field writeonly="true" property="interview.opinion"/></td>
	</tr>
</table>