<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>

<html:html>
<head>
	<title>支付</title>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body style="border-style:none; overflow:auto">
<ext:form action="/submitPayment">
	<table border="0" cellpadding="3" cellspacing="3" align="center" style="width:400px">
		<col align="right" width="100px">
		<col>
		<tr>
			<td nowrap>用 户 名：</td>
			<td><ext:write property="userName" name="SessionInfo" scope="session"/></td>
		</tr>
		<tr>
			<td nowrap>支付原因：</td>
			<td><ext:write property="payReason"/></td>
		</tr>
		<ext:notEmpty property="providerName">
			<tr>
				<td nowrap>服务提供者：</td>
				<td><ext:write property="providerName"/></td>
			</tr>
		</ext:notEmpty>
		<tr>
			<td nowrap>交易金额：</td>
			<td><ext:write property="money"/>元</td>
		</tr>
		<tr>
			<td nowrap>可用余额：</td>
			<td><ext:write property="balance"/>元</td>
		</tr>
		<logic:greaterThan name="payment" property="money" value="0.0"><ext:greaterEqual property="balance" propertyCompare="money">
			<tr>
				<td nowrap>支付密码：</td>
				<td><html:password property="payPassword" styleClass="field required" style="width:160px"/>&nbsp;默认为登录密码</td>
			</tr>
		</ext:greaterEqual></logic:greaterThan>
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
		<tr>
			<td colspan="2" align="center">
				<br>
				<logic:greaterThan name="payment" property="money" value="0.0"><ext:greaterEqual property="balance" propertyCompare="money">
					<input type="button" value="确认" class="button" onclick="FormUtils.submitForm()" />&nbsp;
				</ext:greaterEqual></logic:greaterThan>
				<ext:equal value="dialog" property="act">
					<input type="button" value="取消" class="button" onclick="top.close()" />
				</ext:equal>
				<ext:empty property="act">
					<input type="button" value="取消" class="button" onclick="history.back()" />
				</ext:empty>
			</td>
		</tr>
	</table>
	<html:hidden property="applicationName"/>
	<html:hidden property="payReason"/>
	<html:hidden property="money"/>
	<html:hidden property="providerMoney"/>
	<html:hidden property="providerId"/>
	<html:hidden property="providerName"/>
	<html:hidden property="balance"/>
	<html:hidden property="redirect"/>
	<html:hidden property="from"/>
	<html:hidden property="act"/>
</ext:form>
</body>
</html:html>