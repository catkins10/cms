<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>

<html:html>
<head>
	<title>报酬支付方式</title>
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
    <ext:form action="/saveLaborageMode">
      	<center><div style="width:430px"><%@ include file="/edu/skins/v2/default/detailFormPrompt.jsp" %></div></center>
  		<table border="0" cellpadding="3" cellspacing="3" style="color:#000000; width:430px" align="center">
			<col align="right">
			<col>
			<tr>
				<td nowrap>支付方式：</td>
				<td>
					<html:radio styleClass="radio" property="payMode" value="1" styleId="payModeBank"/>&nbsp;<label for="payModeBank">银行转账</label>&nbsp;
					<html:radio styleClass="radio" property="payMode" value="2" styleId="payModeCash"/>&nbsp;<label for="payModeCash">直接领取现金</label>&nbsp;
					<html:radio styleClass="radio" property="payMode" value="3" styleId="payModeSystem"/>&nbsp;<label for="payModeSystem">转入9191edu帐户</label>&nbsp;
				</td>
			</tr>
			<tr>
				<td nowrap>转账银行：</td>
				<td><html:text property="bank" styleClass="field required"/></td>
			</tr>
			<tr>
				<td nowrap>银行账户名称：</td>
				<td><html:text property="bankAccountName" styleClass="field required"/></td>
			</tr>
			<tr>
				<td>银行帐号：</td>
				<td><html:text property="bankAccount" styleClass="field required"/></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<br>
					<input type="button" value="保存" class="button" onclick="FormUtils.submitForm()">
				</td>
			</tr>
		</table>
    </ext:form>
</body>
</html:html>
