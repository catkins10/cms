<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/submitNewPassword">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/passwordStrength.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/sso/js/changePassword.js"></script>
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000" width="100%">
		<tr>
			<td nowrap="nowrap" align="right">用户名：</td>
			<td width="100%"><ext:field property="userName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">输入原密码：</td>
			<td><ext:field property="oldPassword"/></td>
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
		<tr>
			<td align="center" style="padding-top: 3px; color: #ff0000" colspan="2"><jsp:include page="/jeaf/form/promptAsText.jsp"/> </td>
		</tr>
	</table>
</ext:form>