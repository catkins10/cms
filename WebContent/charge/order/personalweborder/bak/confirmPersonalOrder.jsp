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
<ext:form action="/submitPersonalWebOrder">
<center>
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000;" align="center">
		<col align="right">
		<col>
		<tr>
			<td nowrap>服务内容：</td>
			<td><ext:write property="selectedServicePriceNames"/></td>
		</tr>
		<tr style="display:none">
			<td nowrap>截止时间：</td>
			<td>
				<input type="radio" name="endMode" class="radio" checked="<ext:empty property="serviceEnd">true</ext:empty>" onclick="if(checked){document.getElementById('trServiceEnd').style.display='none';document.getElementsByName('serviceEnd')[0].value='';}" id="endModeNoLimit"><label for="endModeNoLimit">&nbsp;不限制</label>
				<input type="radio" name="endMode" class="radio" checked="<ext:notEmpty property="serviceEnd">true</ext:notEmpty>" onclick="if(checked)document.getElementById('trServiceEnd').style.display='';" id="endModeCustom"><label for="endModeCustom">&nbsp;指定时间</label>
			</td>
		</tr>
		<tr style="<ext:empty property="serviceEnd">display:none</ext:empty>" id="trServiceEnd">
			<td nowrap></td>
			<td><ext:field property="serviceEnd" style="width:200px"/></td>
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
	<input type="button" class="button" value="上一步" onclick="window.history.back();"/>&nbsp;
	<input type="submit" class="button" value="完成"/>&nbsp;
	<input type="button" class="button" value="取消" onclick="window.location='listPersonalOrders.shtml';"/>&nbsp;
</center>
<html:hidden property="selectedServicePriceIds"/>
<html:hidden property="selectedServicePriceNames"/>
<html:hidden property="totalPrice"/>
</ext:form>
</body>
</html:html>