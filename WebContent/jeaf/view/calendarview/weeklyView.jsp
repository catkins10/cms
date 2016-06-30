<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %> 
<%@ page import="com.yuanluesoft.jeaf.view.calendarview.model.CalendarView" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.struts.taglib.TagUtils" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>
<%@page import="com.yuanluesoft.jeaf.view.util.ViewUtils"%>

<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<table width="100%" cellpadding="3" cellspacing="1" style="table-layout:fixed" class="weeklyView">
<%
	ViewPackage viewPackage = (ViewPackage)TagUtils.getInstance().lookup(pageContext, Constants.BEAN_KEY, viewPackageName, null);
	CalendarView calendarView = (CalendarView)viewPackage.getView();
	Calendar today = Calendar.getInstance();
	today.set(Calendar.HOUR_OF_DAY, 0);
 	today.set(Calendar.MINUTE, 0);
 	today.set(Calendar.SECOND, 0);
	today.set(Calendar.MILLISECOND, 0);
	Calendar day = Calendar.getInstance();
	day.setTimeInMillis(viewPackage.getBeginCalendarDate().getTime());
	String[] weeks = new String[]{"一","二","三","四","五","六","日"};
	Object data;
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("M月d日");
	java.text.SimpleDateFormat actionFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	for(int i=0; i<7; i++, day.add(Calendar.DAY_OF_MONTH, 1)) {
%>
		<tr class="<%=(day.equals(today) ? "today" : "day")%>">
			<td valign="top" align="center" class="<%=(i<5 ? "weekDay" : "weekendDay")%>">
<%				if(calendarView.getCalendarAction()!=null) {
					String action = calendarView.getCalendarAction().replaceAll("\\x7bCALENDAR_TIMESTAMP\\x7d", actionFormatter.format(day.getTime()) + " 0:0:0");
					action = action.replaceAll("\\x7bCALENDAR_DATE\\x7d", actionFormatter.format(day.getTime()));
					out.println("<span style=\"cursor:pointer\" onclick=\"" + action + "\">");
				}
				out.println("<div class=\"dateText\">" + formatter.format(day.getTime()) + "</div>");
				out.println("<div class=\"weekText\">星期" + weeks[i] + "</div>");
				if(calendarView.getCalendarAction()!=null) {
					out.println("</span>");
				} %>
			</td>
			<td valign="top">
<%			if((data=ViewUtils.findRecordsByDay(viewPackage.getRecords(), calendarView.getCalendarColumn(), day.get(Calendar.DAY_OF_MONTH), day.get(Calendar.DAY_OF_MONTH)))!=null) {
				request.setAttribute("CALENDAR_DATA", data); %>
				<jsp:include page="calendarRows.jsp" />
<%			} %>
			</td>
		</tr>
<%	}%>
</table>