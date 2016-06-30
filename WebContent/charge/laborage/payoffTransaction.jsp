<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>报酬</title>
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
    <ext:form action="/displayPayoffTransaction">
		<table border="0" cellpadding="3" cellspacing="3" style="color:#000000;width:430px" align="center">
			<col align="right" width="50%" style="font-weight:bold">
			<col width="50%">
			<tr>
				<td nowrap>报　　酬：</td>
				<td><ext:write property="monthlyLaborage.totalLaborage"/>元</td>
			</tr>
			<tr>
				<td nowrap>税　　率：</td>
				<td><ext:write property="monthlyLaborage.taxRateTitle"/></td>
			</tr>
			<tr>
				<td nowrap>实际报酬：</td>
				<td><ext:write property="monthlyLaborage.moneyPay"/>元</td>
			</tr>
			<tr>
				<td nowrap>开始时间：</td>
				<td><ext:write property="monthlyLaborage.beginDate"/></td>
			</tr>
			<tr>
				<td nowrap>结束时间：</td>
				<td><ext:write property="monthlyLaborage.endDate"/></td>
			</tr>
			<tr>
				<td>支付时间：</td>
				<td><ext:write property="monthlyLaborage.payTime" format="yyyy-MM-dd HH:mm"/></td>
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