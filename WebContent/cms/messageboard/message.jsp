<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<ext:equal value="create" property="act">
	<ext:page applicationName="<%=(request.getParameter("forApplication")==null || request.getParameter("forApplication").isEmpty() ? "cms/messageboard" : request.getParameter("forApplication"))%>" pageName="message"/>
</ext:equal>
<ext:equal value="1" property="publicPass">
	<ext:page applicationName="cms/messageboard" pageName="fullyMessage"/>
</ext:equal>