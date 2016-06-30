<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:equal name="user" property="isCompany" value="1">
	<ext:page applicationName="logistics/usermanage" pageName="registCompany"/>
</ext:equal>
<ext:notEqual name="user" property="isCompany" value="1">
	<ext:page applicationName="logistics/usermanage" pageName="registPerson"/>
</ext:notEqual>