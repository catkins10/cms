<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("totalByOrg", "true");%>
<jsp:include flush="true" page="evaluationTotal.jsp"/>