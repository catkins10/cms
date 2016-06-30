<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html:html>
<head>
	<LINK href="<%=request.getContextPath()%>/jeai/usermanage/css/usermanage.css" type="text/css" rel="stylesheet">
</head>
<body>
	<ext:form action="/displayPersonalFinance">
		<table border="0" cellpadding="3" cellspacing="3" style="color:#000000">
  			<col align="right" style="font-weight:bold">
  			<col>
			<tr>
				<td>用 户 名：</td>
				<td><ext:write name="SessionInfo" property="userName" scope="session"/></td>
			</tr>
			<tr>
				<td>账户余额：</td>
				<td><ext:write property="balance"/>元</td>
			</tr>
			<tr>
				<td>可支付余额：</td>
				<td><ext:write property="payableBalance"/>元</td>
			</tr>
		</table>
	</ext:form>
</body>
</html:html>