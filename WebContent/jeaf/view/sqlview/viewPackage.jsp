<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@page import="com.yuanluesoft.jeaf.view.sqlview.model.SqlViewPackage"%>

<%
String viewPackageName = (String)request.getAttribute(SqlViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/view/js/view.js"></script>
<jsp:include page="viewCommon.jsp" />
<jsp:include page="view.jsp" />
<jsp:include page="viewPageBar.jsp" />