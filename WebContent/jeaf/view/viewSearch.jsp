<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
String viewPackageName = (String)request.getAttribute(com.yuanluesoft.jeaf.view.model.ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<jsp:include page="viewSearchConditions.jsp" flush="true"/>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<tr>
		<td colspan="5" height="8px"></td>
	</tr>
	<tr>
		<td width="100%"></td>
		<td><input class="button" style="width:80px" type="button" value="增加条件" onclick="addCondition()"></td>
		<td><input class="button" style="width:80px" type="button" value="删除条件" onclick="deleteCondition()"></td>
		<td><input class="button" style="width:80px" type="button" value="开始搜索" onclick="startSearch('<%=viewPackageName%>')"></td>
		<td><input class="button" style="width:80px" type="button" value="结束搜索" onclick="finishSearch('<%=viewPackageName%>')"></td>
	</tr>
</table>