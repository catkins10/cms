<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
String viewPackageName = (String)request.getAttribute(com.yuanluesoft.jeaf.view.model.ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr valign="bottom">
		<td nowrap align="left" class="previousMonth">&nbsp;<a class="link" href="javascript:switchDate('<%=viewPackageName%>', 'prev')">&lt;&lt;</a></td>
		<td nowrap align="center" class="currentMonth"><ext:write property="<%=viewPackageName + ".beginCalendarDate" %>" format="yyyy年MM月"/></td>
		<td nowrap align="right" class="nextMonth">&nbsp;<a class="link" href="javascript:switchDate('<%=viewPackageName%>', 'next')">&gt;&gt;</a></td>
	</tr>
</table>
<html:hidden property="<%=viewPackageName + ".calendarMode" %>"/>
<html:hidden property="<%=viewPackageName + ".beginCalendarDate" %>"/>