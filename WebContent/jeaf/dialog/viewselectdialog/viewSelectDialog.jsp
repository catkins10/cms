<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/js/selectDialog.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/viewselectdialog/js/viewSelectDialog.js"></script>
<%	request.setAttribute("rightConditionBar", "/jeaf/dialog/viewselectdialog/categoryBar.jsp");
	request.setAttribute("dataPage", "/jeaf/dialog/viewselectdialog/dataPage.jsp"); %>
	<jsp:include page="/jeaf/dialog/selectDialogCommon_m.jsp" />
	<script>loadSelectViewData()</script>
</ext:form>