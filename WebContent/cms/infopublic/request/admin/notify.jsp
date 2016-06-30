<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:iterate id="requestNotify" property="notify">
	<ext:write name="requestNotify" property="notify" filter="false"/>
</ext:iterate>