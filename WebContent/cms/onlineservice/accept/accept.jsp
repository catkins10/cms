<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveAccept" onsubmit="return formOnSubmit();" applicationName="cms/onlineservice/accept" pageName="onlineServiceAccept">
	<ext:subForm/>
	<br>
	<center>
		<ext:iterate id="action" property="formActions">
<%			com.yuanluesoft.jeaf.form.model.FormAction formAction = (com.yuanluesoft.jeaf.form.model.FormAction)pageContext.getAttribute("action"); %>
			<ext:button name="<%=formAction.getTitle()%>" onclick="<%=formAction.getExecute()%>"/>
		</ext:iterate>
	</center>
	<html:hidden property="directoryId"/>
</ext:form>