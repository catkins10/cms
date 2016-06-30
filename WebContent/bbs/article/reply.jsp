<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form siteFormServiceName="bbsFormService" applicationName="bbs" action="/saveReply" onsubmit="return formOnSubmit()" pageName="bbsArticle">
	<ext:subForm/>
</ext:form>