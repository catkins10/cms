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
显示方式:&nbsp;<select name="switchMode" style="width:60px" onchange="document.getElementsByName('<%=viewPackageName%>' + '.beginCalendarDate')[0].value='';document.getElementsByName('<%=viewPackageName%>' + '.calendarMode')[0].value=options[selectedIndex].value;refreshView('<%=viewPackageName%>')">
	<option <%="month".equals(viewPackage.getCalendarMode()) ? "selected" : ""%> value="month">按月</option>
	<option <%="week".equals(viewPackage.getCalendarMode()) ? "selected" : ""%> value="week">按周</option>
	<option <%="day".equals(viewPackage.getCalendarMode()) ? "selected" : ""%> value="day">按日</option>
</select>
