<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="20%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field property="sex"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">账号</td>
		<td class="tdcontent"><ext:field property="loginId"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">密码</td>
		<td class="tdcontent"><ext:field property="password"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>