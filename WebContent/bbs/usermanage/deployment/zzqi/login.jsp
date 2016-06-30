<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<ext:form action="/login" applicationName="bbs" pageName="bbsLogin">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/md5.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/sso/js/login.js"></script>
	<div class="loginArea" style="text-align:center">
		用户名：
		<html:text size="32" name="login" property="userName" styleClass="userName" style="width:200px"/><br><br>
		密　码：
		<html:password size="32" name="login" property="password" styleClass="password" style="width:200px" onkeypress="if(event.keyCode==13)doLogin();"/><br><br>
		<ext:button name="登录" width="50px" onclick="document.forms[0].action = RequestUtils.getContextPath() + '/jeaf/sso/submitlogin.shtml'; doLogin();"/>
		<br><br>
		<font color="#FF0000" class="loginError"><jsp:include page="/jeaf/form/promptAsText.jsp"/></font>
		<script>
			<ext:write property="changePasswordScript" filter="false"/>
		</script>
	</div>
</ext:form>