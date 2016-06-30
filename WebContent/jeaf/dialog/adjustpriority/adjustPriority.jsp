<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/js/selectDialog.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/listdialog/js/listDialog.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/adjustpriority/js/adjustPriority.js"></script>
<%	request.setAttribute(com.yuanluesoft.jeaf.view.model.ViewPackage.VIEW_PACKAGE_NAME, "viewPackage");
	request.setAttribute("dataPage", "/jeaf/dialog/adjustpriority/sourceView.jsp");
	request.setAttribute("hideConditionBar", "true"); %>
	<div id="priorityView" style="display:none">
		<jsp:include page="priorityView.jsp" />
	</div>
	<jsp:include page="/jeaf/dialog/selectDialogCommon_adjustpriority_mobile.jsp" />
	<html:hidden property="selectedRecordIds"/>
</ext:form>