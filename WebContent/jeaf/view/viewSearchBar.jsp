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

<div class="viewSearchBar">
	<div style="float:left" class="searchCondition">
		<html:text styleClass="searchConditionInput" property="<%=viewPackageName + ".quickFilter" %>" onkeypress="if(event.keyCode==13)quickFilter('viewPackage');"/>
	</div>
	<div onclick="quickFilter('<%=viewPackageName%>');" style="float:left" class="searchButtonImage"></div>
	<div onclick="quickFilter('<%=viewPackageName%>');" style="float:left;cursor:hand" class="searchButtonText">搜索</div>
	<ext:notEqual value="true" property="<%=viewPackageName + ".searchMode" %>">
		<div onclick="switchToSearch('<%=viewPackageName%>');" style="float:left" class="advanceSearchButtonImage"></div>
		<div onclick="switchToSearch('<%=viewPackageName%>');" style="float:left;cursor:hand" class="advanceSearchButtonText">高级搜索</div>
	</ext:notEqual>
	<ext:equal value="true" property="<%=viewPackageName + ".searchMode" %>">
		<div onclick="finishSearch('<%=viewPackageName%>');" style="float:left" class="finishSearchButtonImage"></div>
		<div onclick="finishSearch('<%=viewPackageName%>');" style="float:left;cursor:hand" class="finishSearchButtonText">结束搜索</div>
	</ext:equal>
</div>