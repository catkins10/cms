<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:notEmpty name="siteApplicationConfigView" property="siteTree.rootNode.childNodes">
	<%request.setAttribute("viewCategory", "/cms/sitemanage/siteTree.jsp"); %>
</ext:notEmpty>
<jsp:include page="/jeaf/application/applicationView.jsp"/>