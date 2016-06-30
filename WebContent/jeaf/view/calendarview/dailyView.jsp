<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%@ page import="com.yuanluesoft.jeaf.view.calendarview.model.CalendarView" %> 
<%@ page import="java.util.Calendar" %>
<%@ page import="java.sql.Date" %>
<%@ page import="org.apache.struts.taglib.TagUtils" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>
<%@ page import="com.yuanluesoft.jeaf.view.util.ViewUtils"%>

<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<table width="100%" cellpadding="3" cellspacing="1" style="table-layout:fixed" class="dayView">
<%
	ViewPackage viewPackage = (ViewPackage)TagUtils.getInstance().lookup(pageContext, Constants.BEAN_KEY, viewPackageName, null);
	CalendarView calendarView = (CalendarView)viewPackage.getView();
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(viewPackage.getBeginCalendarDate().getTime());
	Calendar now = Calendar.getInstance();
	int hour = -1;
	if(now.get(Calendar.YEAR)==calendar.get(Calendar.YEAR) && 
	   now.get(Calendar.MONTH)==calendar.get(Calendar.MONTH) &&
	   now.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH)) {
		hour = now.get(Calendar.HOUR_OF_DAY);
	}
	String dayText = (new java.text.SimpleDateFormat("MM月dd日")).format(calendar.getTime());
	String dayFull = (new java.text.SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());
	Object data;
	for(int i=8; i<=20; i++, calendar.add(Calendar.DAY_OF_MONTH, 1)) {
%>
		<tr class="hour">
			<td valign="top" align="center" style="width:120px" class="head">
<%				if(calendarView.getCalendarAction()!=null) {
					String action = calendarView.getCalendarAction().replaceAll("\\x7bCALENDAR_TIMESTAMP\\x7d", dayFull + " " + i + ":0:0");
					action = action.replaceAll("\\x7bCALENDAR_DATE\\x7d", dayFull);
					out.println("<span style=\"cursor:pointer\" onclick=\"" + action + "\">");
				} %>
				<span class="dateText"><%=dayText%></span><span class="hourText">&nbsp;<%=(i<10 ? "0" : "") + i%>:00</span>
<%				if(calendarView.getCalendarAction()!=null) {
					out.println("</span>");
				} %>
			</td>
			<td valign="top" class="<%=hour==i ? "contentNow" : "content"%>">
<%			data = ViewUtils.findRecordsByHour(viewPackage.getRecords(), calendarView.getCalendarColumn(), (i==8 ? 0 : i), (i==20 ? 24 : i));
			if(data!=null) {
				request.setAttribute("CALENDAR_DATA", data); %>
				<jsp:include page="calendarRows.jsp" />
<%			} %>
			</td>
		</tr>
<%	}%>
</table>