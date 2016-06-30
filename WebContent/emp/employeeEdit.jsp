<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col align="left">
	<col width="50%">
	<col align="left">
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field property="employ.empname"/></td>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field property="employ.sex"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在部门</td>
		<td class="tdcontent"><ext:field property="employ.departId"/></td>
		<td class="tdtitle" nowrap="nowrap">年龄</td>
		<td class="tdcontent"><ext:field property="employ.age"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">出生日期</td>
		<td class="tdcontent"><ext:field property="employ.birthday"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="employ.phone"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系地址</td>
		<td class="tdcontent"><ext:field property="employ.address"/></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field property="employ.email"/></td>
	</tr>
</table>
