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

<script>
var calendarTimer;
function showRecords(src) {
    var topWindow = getCalendarTopWindow();
	var bar = topWindow.document.getElementById("calendarRecordsBar");
	if(!bar) {
		bar = topWindow.document.createElement("div");
		bar.style.display = "none";
		bar.style.position = "absolute";
		bar.style.width = 1;
		bar.style.height = 1;
		bar.id = "calendarRecordsBar";
		topWindow.document.body.appendChild(bar);
		bar.innerHTML = '<iframe name="calendarRecordsFrame" style="width:1px;height:1px"></iframe>';
		var doc = topWindow.frames["calendarRecordsFrame"].document;
		doc.open();
		doc.write('<html><head>');
		var links = document.getElementsByTagName("link");
		if(links.length>0) {
			for(var i=0; i<links.length; i++) {
				doc.write('<link href="' + links[i].href + '"' + (links[i].rel ? ' rel="' + links[i].rel + '"' : '') + (links[i].type ? ' type="' + links[i].type + '"' : '') + '>');
			}
		}
		var styles = document.getElementsByTagName("style");
		if(styles.length>0) {
			for(var i=0; i<styles.length; i++) {
				doc.write('<style>' + styles[i].innerHTML + '</style>');
			}
		}
		doc.write('</head>');
		doc.write('<body class="calendarRecords" style="background:#ffffe1;border:#c0c0c0 1px solid; padding-left:3px; padding-top:3px; padding-right:3px; overflow:hidden; margin:0px"></body></html>');
		doc.close();
		doc.body.onmousemove = function() {
			if(calendarTimer) {
		    	window.clearTimeout(calendarTimer);
		    }
		    calendarTimer = window.setTimeout('hideCalendarRecordsBar()', 8000);
	    }
	}
	var iframe = bar.getElementsByTagName("iframe")[0];
	iframe.style.width = bar.style.width = 1;
	iframe.style.height = bar.style.height = 1;
	bar.style.visibility = "hidden";
	bar.style.display = "";
	var body = topWindow.frames["calendarRecordsFrame"].document.body;
    var div = src.getElementsByTagName("div")[0];
    body.innerHTML = div.innerHTML;
    var pos = getElementAbsolutePosition(src);
    window.setTimeout('showCalendarRecordsBar(' + pos.left + ',' + (pos.top + src.offsetHeight) + ')', 10);
    if(calendarTimer) {
    	window.clearTimeout(calendarTimer);
    }
    calendarTimer = window.setTimeout('hideCalendarRecordsBar()', 8000);
}
function hideCalendarRecordsBar() {
	var bar = getCalendarTopWindow().document.getElementById("calendarRecordsBar");
	if(bar) {
		bar.style.display = "none";
	}
}
function showCalendarRecordsBar(left, top) {
	var topWindow = getCalendarTopWindow();
	var body = topWindow.frames["calendarRecordsFrame"].document.body;
	var bar = topWindow.document.getElementById("calendarRecordsBar");
	var iframe = bar.getElementsByTagName("iframe")[0];
	var width = body.scrollWidth + 6;
	iframe.style.width = bar.style.width = width;
	iframe.style.height = bar.style.height = body.scrollHeight + 6;
	if(left + width > topWindow.document.body.offsetWidth - 30) {
		left = topWindow.document.body.offsetWidth - 30 - width;
	}
	bar.style.left = left;
	bar.style.top = top;
	bar.style.visibility = "visible";
	try {
		body.focus();
	}
	catch(e) {
	
	}
}
function getCalendarTopWindow() {
	var topWindow = window;
	while(topWindow!=window.top && topWindow.frameElement && topWindow.frameElement.id!="dialogBody") {
		topWindow = topWindow.frameElement.ownerDocument.parentWindow;
	}
	return topWindow;
}
function getElementAbsolutePosition(obj) { 
	var pos = new Object();
	pos.left = 0;
	pos.top = 0;
	while(true) {
		if(obj.tagName=="BODY") {
			if(!obj.ownerDocument.parentWindow.frameElement || obj.ownerDocument.parentWindow.frameElement.id=="dialogBody") {
				break;
			}
			pos.left -= obj.scrollLeft;
			pos.top -= obj.scrollTop;
			obj = obj.ownerDocument.parentWindow.frameElement;
		}
		pos.left += obj.offsetLeft;
		pos.top += obj.offsetTop;
		obj = obj.offsetParent;
	}
	return pos;
}
</script>

<table border="0" width="100%" cellpadding="3" cellspacing="1" style="table-layout:fixed" class="simpleMonthView">
	<tr height="20px">
		<td width="14.3%" align="center" class="weekend" style="color:red">日</td>
		<td width="14.3%" align="center" class="week">一</td>
		<td width="14.3%" align="center" class="week">二</td>
		<td width="14.3%" align="center" class="week">三</td>
		<td width="14.3%" align="center" class="week">四</td>
		<td width="14.3%" align="center" class="week">五</td>
		<td width="14.3%" align="center" class="weekend" style="color:red">六</td>
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
	String month = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
	for(int i=1; i<=loop; i++) {
		if((i-1)%7==0) {
%>
			<tr>
<%		}
		if(i<=week || i>week+days) { %>
			<td class="day"></td>
<%		}
		else {
			out.print("<td align=\"center\" class=\"" + (i-week==today ? "today" : "day") + "\"");
			data = ViewUtils.findRecordsByDay(viewPackage.getRecords(), calendarView.getCalendarColumn(), i-week, i-week);
			if(data!=null && !data.isEmpty()) {
				request.setAttribute("CALENDAR_DATA", data);
				out.print(" onmouseover=\"showRecords(this)\" style=\"color:red;cursor:pointer\"");
			}
			else {
				out.print(" onmouseover=\"hideCalendarRecordsBar()\"");
			}
			out.println(">");
			if(calendarView.getCalendarAction()!=null) {
				String action = calendarView.getCalendarAction().replaceAll("\\x7bCALENDAR_TIMESTAMP\\x7d", month + "-" + (i-week) + " 0:0:0");
				action = action.replaceAll("\\x7bCALENDAR_DATE\\x7d", month + "-" + (i-week) + "-" + (i-week));
				out.println("<span style=\"cursor:pointer\" onclick=\"" + action + "\">" + (i-week) + "</span>");
			}
			else {
				out.println(i-week);
			}
			if(data!=null) { %>
				<div style="display:none">
					<jsp:include page="calendarRows.jsp" />
				</div>
<%			}
			out.println("</td>");
		}
		if(i%7==0) { %>
			</tr>
<%		}
	} %>
</table>