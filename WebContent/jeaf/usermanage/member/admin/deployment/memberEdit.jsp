<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">用户名</td>
		<td class="tdcontent"><ext:field property="loginName"/></td>
		<td class="tdtitle" nowrap="nowrap">密码</td>
		<td class="tdcontent"><ext:field property="password"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><ext:field property="email"/></td>
		<td class="tdtitle" nowrap="nowrap">注册时间</td>
		<td class="tdcontent"><ext:field property="registTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">是否停用</td>
		<td class="tdcontent" colspan="3"><ext:field property="halt"/></td>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent"><ext:field property="company"/></td>
		<td class="tdtitle" nowrap="nowrap">证照号</td>
		<td class="tdcontent"><ext:field property="identityCard"/>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="telephone"/></td>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field property="cell"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="address"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="fax"/></td>
	</tr>
</table>