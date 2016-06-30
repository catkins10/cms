<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/completeInfoFilter">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="50%">
		<col align="right">
		<col width="50%">
		<tr>
			<td nowrap="nowrap">刊型：</td>
			<td colspan="3"><ext:field property="magazineIds"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">采用级别：</td>
			<td><ext:field property="level"/></td>
			<td nowrap="nowrap">是否简讯：</td>
			<td><ext:field property="isBrief"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">是否核实：</td>
			<td><ext:field property="isVerified"/></td>
			<td nowrap="nowrap">是否通报：</td>
			<td><ext:field property="isCircular"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top">筛选意见：</td>
			<td colspan="3"><ext:field property="filterOpinion"/></td>
		</tr>
	</table>
</ext:form>