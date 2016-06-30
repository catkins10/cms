<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>
<%@ page import="org.apache.commons.beanutils.PropertyUtils" %>

<link type="text/css" href="<%=request.getContextPath()%>/jeaf/view/css/view.css" rel="stylesheet">
<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/view/js/view.js"></script>

<!-- 引入js文件 -->
<ext:iterate id="jsFile" property="<%=viewPackageName + ".view.importJavaScripts"%>">
	<script language="JavaScript" charset="gbk" src="<%=request.getContextPath()%>/<ext:write name="jsFile"/>"></script>
</ext:iterate>
<jsp:include page="/jeaf/view/viewCommon.jsp" />
<ext:notEmpty property="<%=viewPackageName + ".view.viewPage"%>">
	<jsp:include page="<%="/jeaf/view/" + PropertyUtils.getProperty(request.getAttribute(Constants.BEAN_KEY), viewPackageName + ".view.viewPage")%>"/>
</ext:notEmpty>

<ext:empty property="<%=viewPackageName + ".view.viewPage"%>">
<div class="viewPackage">
<div class="viewActionAndSearchBar">
	<jsp:include page="/jeaf/view/viewActionBar.jsp" />
	<jsp:include page="/jeaf/view/viewSearchBar.jsp" />
</div>
<ext:notEqual value="condition" property="<%=viewPackageName + ".viewMode"%>">
	<ext:instanceof className="View" property="<%=viewPackageName + ".view"%>">
		<div class="viewPageAndCategoryBar">
			<jsp:include page="/jeaf/view/viewPageBar.jsp" />
		</div>
		<jsp:include page="/jeaf/view/view.jsp" />
		<div class="viewPageAndCategoryBottomBar">
			<jsp:include page="/jeaf/view/viewPageBar.jsp" />
		</div>
	</ext:instanceof>
	<ext:instanceof className="StatisticView" property="<%=viewPackageName + ".view"%>">
		<jsp:include page="/jeaf/view/statisticview/statisticView.jsp" />
		<jsp:include page="/jeaf/view/pagesBar.jsp" />
	</ext:instanceof>
	<ext:instanceof className="CalendarView" property="<%=viewPackageName + ".view"%>">
		<ext:equal value="month" property="<%=viewPackageName + ".calendarMode"%>">
			<jsp:include page="/jeaf/view/calendarview/monthView.jsp" />
			<jsp:include page="/jeaf/view/calendarview/monthSwitch.jsp" />
		</ext:equal>
		<ext:equal value="week" property="<%=viewPackageName + ".calendarMode"%>">
			<jsp:include page="/jeaf/view/calendarview/weeklyView.jsp" />
			<jsp:include page="/jeaf/view/calendarview/weekSwitch.jsp" />
		</ext:equal>
		<ext:equal value="day" property="<%=viewPackageName + ".calendarMode"%>">
			<jsp:include page="/jeaf/view/calendarview/dailyView.jsp" />
			<jsp:include page="/jeaf/view/calendarview/daySwitch.jsp" />
		</ext:equal>
	</ext:instanceof>
</ext:notEqual>
</div>
</ext:empty>
<ext:equal value="condition" property="<%=viewPackageName + ".viewMode"%>">
	<ext:empty property="<%=viewPackageName + ".view.searchPage"%>"><jsp:include page="/jeaf/view/viewSearch.jsp" /></ext:empty>
	<ext:notEmpty property="<%=viewPackageName + ".view.searchPage"%>"><jsp:include page="<%="/jeaf/view/" + PropertyUtils.getProperty(request.getAttribute(Constants.BEAN_KEY), viewPackageName + ".view.searchPage")%>" /></ext:notEmpty>
</ext:equal>