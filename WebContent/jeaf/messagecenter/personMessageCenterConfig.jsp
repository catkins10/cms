<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%
	org.apache.struts.taglib.TagUtils tagUtils = org.apache.struts.taglib.TagUtils.getInstance();
%>
<ext:form action="/saveConfig">
	<table valign="middle" id="tabmsn" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col valign="middle" width="100%">
	   	<ext:iterate id="sender" indexId="senderIndex" name="personMessageCenterConfig" property="senders">
   			<jsp:include page="<%=(String)tagUtils.lookup(pageContext, "sender", "name", null) + "Custom.jsp"%>" />
		</ext:iterate>
	</table>
</ext:form>