<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveTask">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/capture/js/captureRule.js"></script>
   	<ext:tab/>
</ext:form>