<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>

<link type="text/css" href="<%=request.getContextPath()%>/jeaf/common/css/calendar-system.css" media="all" rel="stylesheet">
<link type="text/css" href="<%=request.getContextPath()%>/jeaf/common/css/calendar-win2k-1.css" media="all" rel="stylesheet">
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/calendar.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/calendar-zh.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/calendar-setup.js"></script>

<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr height="10"><td></td></tr>
<tr valign="bottom">
	<td width="100%"><jsp:include page="calendarDisplayMode.jsp" /></td>
	<td nowrap>共<ext:equal value="true" property="<%=viewPackageName + ".searchMode"%>">搜索到 </ext:equal> <ext:write property="<%=viewPackageName + ".recordCount"%>"/> 条记录</td>
	<td nowrap>&nbsp;<a class="link" href="javascript:switchDate('<%=viewPackageName%>', 'prev')">前一天</a></td>
	<td nowrap style="padding-right:8px">&nbsp;<a class="link" href="javascript:switchDate('<%=viewPackageName%>', 'next')">后一天</a></td>
	<td nowrap style="padding-right:3px"><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr>
		<td width="100%"><ext:field onchange="switchDate(name.substring(0, name.indexOf('.')), value)" property="<%=viewPackageName + ".beginCalendarDate"%>" style="width:100px"/></td>
	</tr></table></td>
</tr></table>
<html:hidden property="<%=viewPackageName + ".calendarMode" %>"/>