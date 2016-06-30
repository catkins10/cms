<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html:html>
<head>
	<title>服务退订</title>
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
<ext:form action="/doCancelOrderItem">
<center>
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000;" align="center">
		<col align="right">
		<col>
		<tr>
			<td nowrap>服务内容：</td>
			<td><ext:write property="servicePriceName"/></td>
		</tr>
		<tr>
			<td nowrap>支付密码：</td>
			<td><html:password property="payPassword" style="width:200px"/>&nbsp;默认为登录密码</td>
		</tr>
		<ext:notEmpty property="errors">
			<tr>
				<td valign="top">系统提示：</td>
				<td style="color:#FF0000">
					<ext:iterate property="errors" id="errorMessage" indexId="errorIndex">
						<pre style="margin:0;word-wrap:break-word"><ext:write name="errorMessage"/></pre>
					</ext:iterate>
				</td>
			</tr>
		</ext:notEmpty>
	</table>
	<br>
	<input type="submit" class="button" value="退订"/>
</center>
<html:hidden property="orderItemId"/>
<html:hidden property="servicePriceName"/>
</ext:form>
</body>
</html:html>