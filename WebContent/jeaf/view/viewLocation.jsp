<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<div class="viewLocationBar">
	<span class="viewLocationHeader"></span><a href="<%=request.getContextPath()%>/jeaf/usermanage/personalIndex.shtml" target="_top" class="viewLocationText">首页</a><ext:iterate id="location" property="<%=viewPackageName + ".location"%>"><span class="viewLocationSeparateImage"></span><span class="viewLocationSeparateText">/</span><span class="viewLocationText"><ext:write name="location"/></span></ext:iterate>
</div>