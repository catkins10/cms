<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>我的报酬</title>
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
    <ext:form action="/receiveMonthlyLaborage">
		<table border="0" cellpadding="3" cellspacing="3" style="color:#000000;width:430px" align="center">
			<col align="right" width="50%" style="font-weight:bold">
			<col width="50%">
			<tr>
				<td nowrap>报　　酬：</td>
				<td><ext:write property="totalLaborage"/>元</td>
			</tr>
			<tr>
				<td nowrap>税　　率：</td>
				<td><ext:write property="taxRateTitle"/></td>
			</tr>
			<tr>
				<td nowrap>实际报酬：</td>
				<td><ext:write property="moneyPay"/>元</td>
			</tr>
			<tr>
				<td nowrap>银行帐号：</td>
				<td><ext:write property="bankAccount"/></td>
			</tr>
			<tr>
				<td nowrap>银行账户：</td>
				<td><ext:write property="bankAccountName"/></td>
			</tr>
			<tr>
				<td nowrap>转账银行：</td>
				<td><ext:write property="bank"/></td>
			</tr>
			<tr>
				<td nowrap>开始时间：</td>
				<td><ext:write property="beginDate"/></td>
			</tr>
			<tr>
				<td nowrap>结束时间：</td>
				<td><ext:write property="endDate"/></td>
			</tr>
			<tr>
				<td>支付时间：</td>
				<td><ext:write property="payTime" format="yyyy-MM-dd HH:mm"/></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<br><br>
					<input type="button" class="button" value="返回" onclick="history.back();"/>
				</td>
			</tr>
		</table>
    </ext:form>
</body>
</html:html>
