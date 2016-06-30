<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>
<%@ page import="org.apache.commons.beanutils.PropertyUtils" %>

<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/view/js/view.js"></script>
<jsp:include page="/jeaf/view/viewCommon.jsp" />

<ext:notEqual value="condition" property="<%=viewPackageName + ".viewMode"%>">
	<logic:notEmpty name="viewPage">
		<jsp:include page="<%=(String)request.getAttribute("viewPage")%>" />
	</logic:notEmpty>
	<logic:empty name="viewPage">
		<jsp:include page="/jeaf/view/view.jsp" />
	</logic:empty>
	<jsp:include page="viewPageBar.jsp" />
</ext:notEqual>
<ext:equal value="condition" property="<%=viewPackageName + ".viewMode"%>">
	<ext:empty property="<%=viewPackageName + ".view.searchPage"%>"><jsp:include page="/jeaf/view/viewSearch.jsp" /></ext:empty>
	<ext:notEmpty property="<%=viewPackageName + ".view.searchPage"%>"><jsp:include page="<%="../../" + PropertyUtils.getProperty(request.getAttribute(Constants.BEAN_KEY), viewPackageName + ".view.searchPage")%>" /></ext:notEmpty>
</ext:equal>