<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/writeYearReport" target="_blank" method="get">
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000" width="100%">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap>年度：</td>
			<td><html:text property="year" styleClass="field"/></td>
		</tr>
		<tr>
			<td>月份：</td>
			<td><html:text property="month" styleClass="field"/></td>
		</tr>
		<tr>
			<td nowrap>项目级别：</td>
			<td>
				<html:checkbox property="projectLevels" value="省级重点" styleClass="checkbox"/>&nbsp;省级重点&nbsp;&nbsp;
				<html:checkbox property="projectLevels" value="市级重点" styleClass="checkbox"/>&nbsp;市级重点&nbsp;&nbsp;
				<html:checkbox property="projectLevels" value="县级重点" styleClass="checkbox"/>&nbsp;县级重点
			</td>
		</tr>
	</table>
</ext:form>