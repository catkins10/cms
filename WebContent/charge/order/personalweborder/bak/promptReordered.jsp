<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html:html>
<head>
	<title>订购服务</title>
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
<ext:form action="/continueWhenReordered">
<div style="font-color: #ff0000">以下服务，您已经订购过，您必须退订掉原来的服务才可以订购新服务：<div>
<table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="#CCCCCC">
	<tr align="center" valign="middle">
		<td bgcolor="#e9f5f7">服务名称</td>
		<td width="150px" bgcolor="#e9f5f7">订购时间</td>
		<td width="120px" bgcolor="#e9f5f7">服务截止时间</td>
	</tr>
	<ext:iterate id="orderedService" property="orderedServices">
		<tr valign="middle">
			<td bgcolor="#FFFFFF"><ext:write name="orderedService" property="serviceName"/></td>
			<td align="center" bgcolor="#FFFFFF"><ext:write name="orderedService" property="order.orderTime" format="yyyy-MM-dd HH:mm" /></td>
		    <td align="center" bgcolor="#FFFFFF"><ext:write name="orderedService" property="order.serviceEnd" format="yyyy-MM-dd"/></td>
		</tr>
	</ext:iterate>
</table>
<br>
<center>
	<input type="button" class="button" value="上一步" style="width:50px" onclick="window.history.back();"/>&nbsp;
	<input type="submit" class="button" value="继续" style="width:50px; display: none"/>&nbsp;
	<input type="button" class="button" value=“服务退订" style="width:50px" onclick="window.location='listPersonalOrders.shtml';"/>
</center>
<html:hidden property="selectedServicePriceIds"/>
<html:hidden property="selectedServicePriceNames"/>
<html:hidden property="totalPrice"/>
</ext:form>
</body>
</html:html>