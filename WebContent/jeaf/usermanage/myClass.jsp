<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html:html>
<head>
	<title>我的班级</title>
</head>
<frameset style="border:none" bordercolor="#dbdce3" frameborder="0" rows="56,*"  framespacing="0"  border="0">
	<frame src="myClassLogo.jsp" NORESIZE scrolling="none" >
	<frameset style="border:none" bordercolor="#dbdce3" frameborder="1" cols="158,*" framespacing="1"  border="0">
		<frame src="myClassNavigator.shtml" frameborder="0" NORESIZE>
		<frame id="contentFrame" src="listToApprovalStudents.shtml" frameborder="0">
	</frameset>
</frameset>
</html:html>
