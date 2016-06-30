<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveUserPasswordPolicy">
   	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<tr>
			<td class="tdtitle" nowrap="nowrap">内部用户密码强度</td>
			<td class="tdcontent" width="100%"><ext:field property="internalPasswordStrength"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">外部用户密码强度</td>
			<td class="tdcontent"><ext:field property="externalPasswordStrength"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">内部用户密码修改周期(天)</td>
			<td class="tdcontent"><ext:field property="internalPasswordDays"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">外部用户密码修改周期(天)</td>
			<td class="tdcontent"><ext:field property="externalPasswordDays"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">密码输错次数控制</td>
			<td class="tdcontent"><ext:field property="passwordWrong"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">密码重置验证码有效期(分钟)</td>
			<td class="tdcontent"><ext:field property="resetPasswordCodeExpire"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">密码重置邮件标题</td>
			<td class="tdcontent"><ext:field property="resetPasswordMailSubject"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap" valign="top">密码重置邮件内容</td>
			<td class="tdcontent">
				<div style="padding-bottom: 3px">
					<input type="button" class="button" value="插入用户名" onclick="FormUtils.pasteText('resetPasswordMailContent', '&lt;用户名&gt;')">
					<input type="button" class="button" value="插入验证码" onclick="FormUtils.pasteText('resetPasswordMailContent', '&lt;验证码&gt;')">
					<input type="button" class="button" value="插入有效期" onclick="FormUtils.pasteText('resetPasswordMailContent', '&lt;有效期&gt;')">
				</div>
				<ext:field property="resetPasswordMailContent"/>
			</td>
		</tr>
	</table>
</ext:form>