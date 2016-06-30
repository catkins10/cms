<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html:html>
<head>
	<title>个人设置</title>
</head>
<frameset style="border:none" bordercolor="#f8f8fc" frameborder="0" rows="80,*"  framespacing="0"  border="0">
	<frame src="personalSettingLogo.jsp" NORESIZE scrolling="none" >
	<frameset style="border:none" bordercolor="#4296EF" frameborder="1" cols="168,*" framespacing="1"  border="0">
		<frame src="personalSettingMenu.jsp" frameborder="0" NORESIZE>
		<frame id="contentFrame" src="personalData.shtml" frameborder="0">
	</frameset>
</frameset>
</html:html>