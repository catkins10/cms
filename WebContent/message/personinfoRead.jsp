<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:write property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:write property="sex"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">年龄</td>
		<td class="tdcontent"><ext:write property="age"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">出生日期</td>
		<td class="tdcontent"><ext:write property="birthday"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:write property="phone"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:write property="email"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">家庭住址</td>
		<td class="tdcontent"><ext:write property="address"/></td>
	</tr>
</table>