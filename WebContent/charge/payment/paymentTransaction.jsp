<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html:html>
<head>
<title>支付增值费用</title>
</head>
<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
<body>
<ext:form action="/displayPaymentTransaction">
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000;" align="center">
		<col align="right" width="120px" style="font-weight:bold">
		<col>
		<tr>
			<td nowrap>支付目的：</td>
			<td><ext:write property="payReason"/></td>
		</tr>
		<ext:notEmpty property="providerName">
			<tr>
				<td nowrap>服务提供者：</td>
				<td><ext:write property="providerName"/></td>
			</tr>
		</ext:notEmpty>
		<tr>
			<td nowrap>支付时间：</td>
			<td><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
		<tr>
			<td nowrap>支付金额：</td>
			<td><ext:write property="money"/>元</td>
		</tr>
		<tr>
			<td nowrap>帐户余额：</td>
			<td><ext:write property="balance"/>元</td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input type="button" class="button" value="返回" onclick="history.back()"/></td>
		</tr>
	</table>
</ext:form>
</body>
</html:html>