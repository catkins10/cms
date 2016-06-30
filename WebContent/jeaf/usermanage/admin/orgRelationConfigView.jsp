<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:notEmpty name="adminOrgRelationConfigView" property="orgTree.rootNode.childNodes">
	<%request.setAttribute("viewCategory", "/jeaf/usermanage/admin/orgTree.jsp"); %>
</ext:notEmpty>
<jsp:include page="/jeaf/application/applicationView.jsp"/>