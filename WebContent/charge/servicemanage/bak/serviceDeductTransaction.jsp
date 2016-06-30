<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html:html>
<head>
<title><ext:write property="formTitle"/></title>
</head>
<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
<body>
<ext:form action="/displayServiceDeductTransaction">
	<table  border="0" cellpadding="3" cellspacing="3" style="color:#000000" >
		<col align="right" width="50%" style="font-weight:bold">
		<col width="50%">
		<tr>
			<td>服务名称：</td>
			<td><ext:write property="service.serviceName"/></td>
		</tr>
		<tr>
			<td>价　　格：</td>
			<td><ext:write property="servicePrice"/></td>
		</tr>
		<tr>
			<td>服务内容：</td>
			<td><ext:write property="service.itemTitles"/></td>
		</tr>
		<tr>
			<td nowrap>本次扣款：</td>
			<td><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
		<tr>
			<td nowrap>扣款金额：</td>
			<td><ext:write property="money"/>元</td>
		</tr>
		<tr>
			<td nowrap>帐户余额：</td>
			<td><ext:write property="balance"/>元</td>
		</tr>
		<tr>
			<td colspan="2"><input type="button" class="button" value="返回" onclick="history.back()"/></td>
		</tr>
	</table>
</ext:form>
</body>
</html:html>