<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%@ page import="com.yuanluesoft.jeaf.view.calendarview.model.CalendarView" %>
<%@ page import="java.util.Calendar" %> 
<%@ page import="org.apache.struts.taglib.TagUtils" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>
<%@ page import="com.yuanluesoft.jeaf.view.util.ViewUtils"%>
<%@ page import="java.util.List"%>

<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>

<table width="100%" cellpadding="3" cellspacing="1" style="table-layout:fixed" class="monthView">
	<tr height="24px">
		<td width="14.3%" align="center" class="weekend">星期日</td>
		<td width="14.3%" align="center" class="week">星期一</td>
		<td width="14.3%" align="center" class="week">星期二</td>
		<td width="14.3%" align="center" class="week">星期三</td>
		<td width="14.3%" align="center" class="week">星期四</td>
		<td width="14.3%" align="center" class="week">星期五</td>
		<td width="14.3%" align="center" class="weekend">星期六</td>
	</tr>
<%
	ViewPackage viewPackage = (ViewPackage)TagUtils.getInstance().lookup(pageContext, Constants.BEAN_KEY, viewPackageName, null);
	CalendarView calendarView = (CalendarView)viewPackage.getView();
	Calendar calendar = Calendar.getInstance();
	int thisYear = calendar.get(Calendar.YEAR);
	int thisMonth = calendar.get(Calendar.MONTH);
	int today = calendar.get(Calendar.DAY_OF_MONTH);
	calendar.setTimeInMillis(viewPackage.getBeginCalendarDate().getTime());
	if(calendar.get(Calendar.YEAR)!=thisYear || calendar.get(Calendar.MONTH)!=thisMonth) {
		today = -1;
	}
	int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	calendar.add(Calendar.MONTH, 1);
	calendar.add(Calendar.DAY_OF_MONTH, -1);
	int days = calendar.get(Calendar.DAY_OF_MONTH);
	int loop = (week + days) % 7;
	loop = (loop==0 ? week + days : week + days + 7-loop);
	List data;
	String month = calendar.get(Calendar.YEAR) + "-" + com.yuanluesoft.jeaf.util.StringUtils.formatNumber(calendar.get(Calendar.MONTH) + 1, 2, true);
	for(int i=1; i<=loop; i++) {
		if((i-1)%7==0) { %>
			<tr valign="top">
<%		}
		if(i<=week || i>week+days) { %>
			<td class="day"></td>
<%		}
		else {
			if((data=ViewUtils.findRecordsByDay(viewPackage.getRecords(), calendarView.getCalendarColumn(), i-week, i-week))!=null && !data.isEmpty()) {
				request.setAttribute("CALENDAR_DATA", data);
			}%>
			<td class="<%=(i-week==today ? "today":"day")%>">
				<table height="32px" cellpadding="0" cellspacing="0" border="0"><tr valign="top">
					<td>
<%						if(calendarView.getCalendarAction()!=null) {
							String action = calendarView.getCalendarAction().replaceAll("\\x7bCALENDAR_TIMESTAMP\\x7d", month + "-" + com.yuanluesoft.jeaf.util.StringUtils.formatNumber(i-week, 2, true) + " 00:00:00");
							action = action.replaceAll("\\x7bCALENDAR_DATE\\x7d", month + "-" + com.yuanluesoft.jeaf.util.StringUtils.formatNumber(i-week, 2, true));
							out.println("<span style=\"cursor:pointer\" onclick=\"" + action + "\">" + (i-week) + "</span>");
						}
						else {
							out.println((i-week));
						} %>
					</td>
<%					if(data!=null && !data.isEmpty()) { %>
						<td style="padding-left:2px"><jsp:include page="calendarRows.jsp" /></td>
<%					} %>
				</tr></table>
			</td>
<%		}
		if(i%7==0) { %>
			</tr>
<%		}
	}
%>
</table>