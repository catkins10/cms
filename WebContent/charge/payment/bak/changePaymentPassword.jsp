<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<html:html>
<head>
	<title>修改支付密码</title>
	<LINK href="<%=request.getContextPath()%>/jeai/usermanage/css/usermanage.css" type="text/css" rel="stylesheet">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/ratePassword.js"></script>
	<script>
		function doSubmit() {
			if(document.getElementsByName("oldPayPassword")[0].value=='') {
				alert("请输入原支付密码！");
				document.getElementsByName("oldPayPassword")[0].focus();
				return;
			}
			if(document.getElementsByName("newPayPassword")[0].value=='') {
				alert("请输入新支付密码！");
				document.getElementsByName("newPayPassword")[0].focus();
				return;
			}
			if(document.getElementsByName("newPayPassword")[0].value!=document.getElementsByName("newPayPassword")[1].value) {
				alert("密码不一致！");
				document.getElementsByName("newPayPassword")[1].focus();
				return;
			}
			document.forms[0].submit();
		}
	</script>
</head>
<body style="margin-left:5px; margin-right:5px; margin-top:0px; overflow:auto;">
    <ext:form action="/submitNewPaymentPassword">
    	<br>
		<center>
			<table border="0" cellpadding="3" cellspacing="3" style="color:#000000">
				<col align="right">
    			<col>
				<tr>
					<td>用户名：</td>
					<td><ext:write name="SessionInfo" property="userName" scope="session"/></td>
				</tr>
				<tr>
					<td>输入原支付密码：</td>
					<td><html:password property="oldPayPassword" styleClass="textInput"/> 默认为登录密码</td>
				</tr>
				<tr>
					<td>输入新支付密码：</td>
					<td><html:password property="newPayPassword" styleClass="textInput" onkeyup="SetPwdStrengthEx(document.forms[0],this.value);"/></td>
				</tr>
				<tr>
					<td>密码安全等级：</td>
					<td>
						<span style="text-align:center; height:20px; background-color:#EBEBEB">
							<div id="idSM1" style="float:left;width:30px;height:100%">
								<span id="idSMT1" style="display:none;height:100%;padding-top:3px">低</span>
							</div>
							<div id="idSM2" style="float:left;width:88px;height:100%">
								<span id="idSMT0" style="color:#666;padding-top:3px;height:100%">未能评级</span>
								<span id="idSMT2" style="display:none;padding-top:3px;height:100%">中</span>
							</div>
							<div id="idSM3" style="float:left;width:30px;height:100%">
								<span id="idSMT3" style="display:none;padding-top:3px;height:100%">高</span>
							</div>
						</span>
					</td>
				</tr>
				<tr>
					<td>确认新支付密码：</td>
					<td><html:password property="newPayPassword" styleClass="textInput"/></td>
				</tr>
				<ext:notEmpty property="errors">
					<tr>
						<td>系统提示：</td>
						<td style="color:#FF0000">
							<ext:iterate property="errors" id="errorMessage" indexId="errorIndex">
								<pre style="margin:0;word-wrap:break-word"><ext:write name="errorMessage"/></pre>
							</ext:iterate>
						</td>
					</tr>
				</ext:notEmpty>
				<tr>
					<td colspan="2" align="center" style="padding-top:8px">
						<input type="button" class="button" value="确定" onclick="doSubmit();"/>&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</center>
    </ext:form>
</body>
</html:html>
