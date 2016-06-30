<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.yuanluesoft.jeaf.view.sqlview.model.SqlViewPackage;"%>
<%
String viewPackageName = (String)request.getAttribute(SqlViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<html:hidden property="<%=viewPackageName + ".recordCount" %>"/>
<html:hidden property="<%=viewPackageName + ".pageCount" %>"/>
<html:hidden property="<%=viewPackageName + ".page" %>"/>
<html:hidden property="<%=viewPackageName + ".curPage" %>"/>
<input type="hidden" name="<%=viewPackageName + ".viewName"%>" value="<ext:write property="<%=viewPackageName + ".view.name" %>"/>">
<input type="hidden" name="<%=viewPackageName + ".applicationName"%>" value="<ext:write property="<%=viewPackageName + ".view.applicationName" %>"/>">
