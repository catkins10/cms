<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:iterate id="supplement" property="supplements">
	<b>补充通知<ext:notEmpty name="supplement" property="publicBeginTime">(<ext:write name="supplement" property="publicBeginTime" format="yyyy-MM-dd"/>)</ext:notEmpty>：</b><br>
	<ext:write name="supplement" property="body" filter="false"/><br><br>
</ext:iterate>