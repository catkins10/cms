<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	String viewPackageName = (String)request.getAttribute(com.yuanluesoft.jeaf.view.model.ViewPackage.VIEW_PACKAGE_NAME);
	if(viewPackageName==null || viewPackageName.equals("")) {
		viewPackageName = "viewPackage";
	}
%>
<html:html>
<head>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/view/js/view.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/js/subPageCssImport.js"></script>
</head>
<body style="overflow: none">
<ext:form action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>">
	<jsp:include page="/jeaf/view/viewCommon.jsp" />
	<div class="viewLocationAndSearchBar">
		<jsp:include page="/jeaf/view/viewLocation.jsp" />
		<ext:notEmpty name="viewCategory"><jsp:include page="<%=(String)request.getAttribute("viewCategory")%>"/></ext:notEmpty>
		<jsp:include page="/jeaf/view/category.jsp" />
		<jsp:include page="/jeaf/view/viewSearchBar.jsp" />
	</div>
<%
	com.yuanluesoft.jeaf.view.model.View currentView = (com.yuanluesoft.jeaf.view.model.View)org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, org.apache.struts.taglib.html.Constants.BEAN_KEY, viewPackageName + ".view", "request");
	if(currentView.getExtendParameter("extendPage")!=null) { %>
		<div class="viewExtendPage">
			<jsp:include page="<%=(String)currentView.getExtendParameter("extendPage")%>" flush="true"/>
		</div>
<%	} %>
	<ext:notEqual value="condition" property="<%=viewPackageName + ".viewMode"%>">
		<jsp:include page="/jeaf/view/viewActionBar.jsp" />	
		<ext:instanceof className="View" property="<%=viewPackageName + ".view"%>">
			<jsp:include page="/jeaf/view/view.jsp" />
			<jsp:include page="/jeaf/view/viewPageBar.jsp" />
		</ext:instanceof>
		<ext:instanceof className="StatisticView" property="<%=viewPackageName + ".view"%>">
			<jsp:include page="/jeaf/view/statisticview/statisticView.jsp" />
			<div class="viewPageAndCategoryBottomBar">
				<jsp:include page="/jeaf/view/viewPageBar.jsp" />
			</div>
		</ext:instanceof>
		<ext:instanceof className="CalendarView" property="<%=viewPackageName + ".view"%>">
			<div style="padding-top: 6px;">
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
			</div>
		</ext:instanceof>
	</ext:notEqual>
	<ext:equal value="condition" property="<%=viewPackageName + ".viewMode"%>">
		<jsp:include page="/jeaf/view/viewSearch.jsp" />
	</ext:equal>
	<div style="height:180px">&nbsp;</div>
</ext:form>
</body>
</html:html>