<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sex"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">身份证号码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="idcardNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">系部</td>
		<td class="tdcontent"><ext:field writeonly="true" property="department"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">账号/工号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="loginId"/></td>
		<td class="tdtitle" nowrap="nowrap">密码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="password"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建日期</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>