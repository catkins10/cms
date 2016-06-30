<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/resetPassword">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/passwordStrength.js"></script>
	<script>
		function formOnSubmit() {
			if("inputLoginName"==document.getElementsByName('act')[0].value) {
				if(document.getElementsByName("userName")[0].value=='') {
					alert("请输入用户名！");
					document.getElementsByName("userName")[0].focus();
					return false;
				}
				return true;
			}
			if(document.getElementsByName("code")[0].value=='') {
				alert("请输入验证码！");
				document.getElementsByName("cede")[0].focus();
				return false;
			}
			if(document.getElementsByName("newPassword")[0].value=='') {
				alert("请输入新密码！");
				document.getElementsByName("newPassword")[0].focus();
				return false;
			}
			if(document.getElementsByName("newPassword")[0].value!=document.getElementsByName("newPassword")[1].value) {
				alert("密码不一致！");
				document.getElementsByName("newPassword")[1].focus();
				return false;
			}
			var passwordStrength = Number(document.getElementsByName("passwordStrength")[0].value);
			if(passwordStrength>1 && getPasswordStrength(document.getElementsByName("newPassword")[0].value)<passwordStrength) {
				alert("密码安全等级不够！");
				document.getElementsByName("newPassword")[0].focus();
				return false;
			}
			return true;
		}
	</script>
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000" width="100%">
		<ext:equal value="inputLoginName" property="act">
			<tr>
				<td nowrap="nowrap" align="right">用户名：</td>
				<td width="100%"><ext:field property="userName"/></td>
			</tr>
		</ext:equal>
		<ext:equal value="inputNewPassword" property="act">
			<tr>
				<td nowrap="nowrap" align="right" valign="top" style="padding-top:8px">重置验证码：</td>
				<td>
					<ext:field property="code"/>
					<div style="padding-top:3px;">密码重置邮件已发送到您的邮箱:<ext:field writeonly="true" property="mailAddress"/>,请查收获取验证码</div>
					<ext:field style="display:none" property="userName"/>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" align="right">输入新密码：</td>
				<td><ext:field property="newPassword"/></td>
			</tr>
			<tr>
				<td nowrap="nowrap" align="right">密码安全等级：</td>
				<td>
					<span id="passwordStrengthSpan"></span>
					<script>writePasswordStrength(document.getElementsByName('newPassword')[0], document.getElementById('passwordStrengthSpan'));</script>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" align="right">确认新密码：</td>
				<td><ext:field property="newPassword"/></td>
			</tr>
		</ext:equal>
		<tr>
			<td align="center" style="padding-top: 3px; color: #ff0000" colspan="2"><jsp:include page="/jeaf/form/promptAsText.jsp"/> </td>
		</tr>
	</table>
</ext:form>