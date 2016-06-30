<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.struts.taglib.TagUtils" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>

<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
ViewPackage viewPackage = (ViewPackage)TagUtils.getInstance().lookup(pageContext, Constants.BEAN_KEY, viewPackageName, null);
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr height="10"><td></td></tr>
<tr valign="bottom">
	<td width="100%"><jsp:include page="calendarDisplayMode.jsp" /></td>
	<td nowrap>共<ext:equal value="true" property="<%=viewPackageName + ".searchMode"%>">搜索到 </ext:equal> <ext:write property="<%=viewPackageName + ".recordCount"%>"/> 条记录</td>
	<td nowrap>&nbsp;<a class="link" href="javascript:switchDate('<%=viewPackageName%>', 'prev')">上周</a></td>
	<td nowrap>&nbsp;<a class="link" href="javascript:switchDate('<%=viewPackageName%>', 'next')">下周</a></td>
	<td nowrap width="233px" style="padding-right:18px">&nbsp;&nbsp;<select onchange="switchDate('<%=viewPackageName%>', options[selectedIndex].value)">
<%		Calendar thisWeek = Calendar.getInstance();
		thisWeek.setTimeInMillis(viewPackage.getBeginCalendarDate().getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(viewPackage.getBeginCalendarDate().getTime());
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
 		calendar.add(Calendar.DAY_OF_MONTH, (week==1 ? -6 : 2 - week));
 		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd");
 		java.text.SimpleDateFormat valueFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
 		for(int i=1; i<=53; i++) {
 			out.print("<option" + (calendar.equals(thisWeek) ? " selected" : "") + " value=\"" + valueFormatter.format(calendar.getTime()) + "\">");
 			out.print(formatter.format(calendar.getTime()) + " - ");
 			calendar.add(Calendar.DAY_OF_MONTH, 6);
 			out.print(formatter.format(calendar.getTime()) + "&nbsp;&nbsp;(第" + i + "周)");
 			calendar.add(Calendar.DAY_OF_MONTH, 1);
 			out.println("</option>");
		}
%>
	</select></td>
</tr></table>
<html:hidden property="<%=viewPackageName + ".calendarMode" %>"/>
<html:hidden property="<%=viewPackageName + ".beginCalendarDate" %>"/>