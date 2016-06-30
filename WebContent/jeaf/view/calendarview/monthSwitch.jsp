<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
String viewPackageName = (String)request.getAttribute(com.yuanluesoft.jeaf.view.model.ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
com.yuanluesoft.jeaf.view.model.ViewPackage viewPackage = (com.yuanluesoft.jeaf.view.model.ViewPackage)org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, org.apache.struts.taglib.html.Constants.BEAN_KEY, viewPackageName, null);
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr height="10"><td></td></tr>
<tr valign="bottom">
	<td width="100%"><jsp:include page="calendarDisplayMode.jsp" /></td>
	<td nowrap>共<ext:equal value="true" property="<%=viewPackageName + ".searchMode"%>">搜索到 </ext:equal> <ext:write property="<%=viewPackageName + ".recordCount"%>"/> 条记录</td>
	<td nowrap>&nbsp;<a class="link" href="javascript:switchDate('<%=viewPackageName%>', 'prev')">上个月</a></td>
	<td nowrap>&nbsp;<a class="link" href="javascript:switchDate('<%=viewPackageName%>', 'next')">下个月</a></td>
	<td nowrap width="120px" style="padding-right:18px">&nbsp;&nbsp;<select onchange="switchDate('<%=viewPackageName%>', options[selectedIndex].value)">
<%		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTimeInMillis(viewPackage.getBeginCalendarDate().getTime());
		int month = calendar.get(java.util.Calendar.MONTH) + 1;
		int year = calendar.get(java.util.Calendar.YEAR);
 		for(int i=1; i<=12; i++) {
 			out.print("<option" + (i==month ? " selected" : "") + " value=\"" + year + "-" + i + "-1\">");
 			out.print(year + "年" + (i<10 ? "0" : "") + i + "月");
 			out.println("</option>");
		}
%>
	</select></td>
</tr></table>
<html:hidden property="<%=viewPackageName + ".calendarMode" %>"/>
<html:hidden property="<%=viewPackageName + ".beginCalendarDate" %>"/>