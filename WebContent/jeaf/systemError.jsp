<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<%
if(com.yuanluesoft.jeaf.util.RequestUtils.getRequestInfo(request).isClientRequest()) {
	response.setStatus(500);
}
else { %>
	<ext:page applicationName="cms/sitemanage" pageName="systemErrorPrompt" pageServiceName="siteIndexPageService"/>
<% 
} %>