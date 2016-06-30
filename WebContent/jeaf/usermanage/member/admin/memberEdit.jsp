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
		<td class="tdtitle" nowrap="nowrap">真实姓名</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field property="sex"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">证件名称</td>
		<td class="tdcontent"><ext:field property="identityCardName"/></td>
		<td class="tdtitle" nowrap="nowrap">证件号码</td>
		<td class="tdcontent"><ext:field property="identityCard"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属省份</td>
		<td class="tdcontent"><ext:field property="area"/></td>
		<td class="tdtitle" nowrap="nowrap">工作单位/所在院校</td>
		<td class="tdcontent"><ext:field property="company"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位所属行业</td>
		<td class="tdcontent"><ext:field property="organization"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">邮编</td>
		<td class="tdcontent"><ext:field property="postalcode"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="fax"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="telephone"/></td>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field property="cell"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">部门</td>
		<td class="tdcontent"><ext:field property="department"/></td>
		<td class="tdtitle" nowrap="nowrap">职务</td>
		<td class="tdcontent"><ext:field property="duty"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否停用</td>
		<td class="tdcontent"><ext:field property="halt"/></td>
		<td class="tdtitle" nowrap="nowrap">公开用户资料</td>
		<td class="tdcontent"><ext:field property="hideDetail"/></td>
	</tr>
</table>