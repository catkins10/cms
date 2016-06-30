<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html:html>
<head>
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
<ext:form action="/continueWhenInsufficientBalance">
	<center>
		<span class="prompt">您目前的账户余额为<ext:write property="balance"/>，可能不足以支付您订购的服务费用!</span>
		<br>
		<br>
		<input type="button" class="button" value="上一步" style="width:50px" onclick="window.history.back();"/>&nbsp;
		<input type="submit" class="button" value="继续" style="width:50px"/>&nbsp;
		<input type="button" class="button" value="取消" style="width:50px" onclick="window.location='listPersonalOrders.shtml';"/>
	</center>
	<html:hidden property="selectedServicePriceIds"/>
	<html:hidden property="selectedServicePriceNames"/>
	<html:hidden property="totalPrice"/>
</ext:form>
</body>
</html:html>