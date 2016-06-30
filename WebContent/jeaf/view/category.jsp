<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<ext:notEmpty property="<%=viewPackageName + ".categoryTitle"%>">
	<div class="viewCategoryBar">
		<ext:iterate id="categoryTitle" indexId="categoryIndex" property="<%=viewPackageName + ".categoryTitles"%>"><ext:notEmpty name="viewCategory"><ext:notEqual value="0" name="categoryIndex"><span class="viewLocationSeparateText">/</span></ext:notEqual></ext:notEmpty><ext:empty name="viewCategory"><span class="viewLocationSeparateText">/</span></ext:empty><span><ext:write name="categoryTitle"/></span></ext:iterate><span nowrap onclick="selectCategory(this, '<ext:write property="<%=viewPackageName + ".view.applicationName"%>" />', '<ext:write property="<%=viewPackageName + ".view.name"%>"/>', '<ext:write property="<%=viewPackageName + ".categories"%>"/>')" class="viewSelectCategory">&nbsp;</span>
		<div id="divCategoryTree" align="left" onblur="hideCategory(this)" onselectstart="return false;" class="viweCategoryBox" style="overflow:auto; outline: none; display:none">
			<iframe id="categoryTree" width="100%" height="100%" frameborder="0" allowtransparency="true"></iframe>
		</div>
	</div>
</ext:notEmpty>