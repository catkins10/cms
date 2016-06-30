<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>充值卡充值</title>
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
    <ext:form action="/submitCardTopUp">
    	<ext:equal value="3" name="SessionInfo" property="userType">
		   	<center><div style="width:430px"><%@ include file="/edu/skins/v2/default/detailFormPrompt.jsp" %></div></center>
		   	<ext:equal value="0" property="step"><%@ include file="cardTopUpStepI.jsp" %></ext:equal>
	  		<ext:equal value="1" property="step"><%@ include file="cardTopUpStepII.jsp" %></ext:equal>
	  		<html:hidden property="step"/>
  		</ext:equal>
  		<ext:notEqual value="3" name="SessionInfo" property="userType">
  			<center>
	  			<div style="color:#ff0000; padding:30px; font-size:14px; font-weight:bold">充值功能目前只对学生开放。</div>
  			</center>
  		</ext:notEqual>
    </ext:form>
</body>
</html:html>
