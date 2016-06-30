<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveAccount">
   	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<tr>
			<td class="tdtitle" nowrap="nowrap">邮箱</td>
			<td class="tdcontent" width="100%"><ext:field property="mailAddress"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">名称</td>
			<td class="tdcontent"><ext:field property="name"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">SMTP主机</td>
			<td class="tdcontent"><ext:field property="smtpHost"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">SMTP端口</td>
			<td class="tdcontent"><ext:field property="smtpPort"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">SMTP用户名</td>
			<td class="tdcontent"><ext:field property="smtpUserName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">SMTP密码</td>
			<td class="tdcontent"><ext:field property="smtpPassword"/></td>
		</tr>
	</table>
</ext:form>