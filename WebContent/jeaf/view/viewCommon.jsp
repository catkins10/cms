<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<ext:iterate id="viewScript" property="<%=viewPackageName + ".view.scripts"%>">
	<script language="JavaScript" src="<%=request.getContextPath()%><ext:write name="viewScript" filter="false"/>"></script>
</ext:iterate>
<html:hidden property="<%=viewPackageName + ".searchConditions" %>"/>
<html:hidden property="<%=viewPackageName + ".searchConditionsDescribe" %>"/>
<html:hidden property="<%=viewPackageName + ".recordCount" %>"/>
<html:hidden property="<%=viewPackageName + ".pageCount" %>"/>
<html:hidden property="<%=viewPackageName + ".page" %>"/>
<html:hidden property="<%=viewPackageName + ".curPage" %>"/>
<html:hidden property="<%=viewPackageName + ".viewMode" %>"/>
<html:hidden property="<%=viewPackageName + ".searchMode" %>"/>
<html:hidden property="<%=viewPackageName + ".categories" %>"/>
<html:hidden property="<%=viewPackageName + ".categoryCount" %>"/>
<html:hidden property="<%=viewPackageName + ".selectedIds" %>"/>
<input type="hidden" name="<%=viewPackageName + ".viewName"%>" value="<ext:write property="<%=viewPackageName + ".view.name" %>"/>">
<input type="hidden" name="<%=viewPackageName + ".applicationName"%>" value="<ext:write property="<%=viewPackageName + ".view.applicationName" %>"/>">