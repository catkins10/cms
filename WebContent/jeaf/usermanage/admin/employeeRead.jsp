<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:write property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">登录用户名</td>
		<td class="tdcontent">
			<ext:equal value="1" property="preassign">
				<ext:write property="loginName"/>
			</ext:equal>
			<ext:notEqual value="1" property="preassign">
				<ext:write property="loginName"/>
			</ext:notEqual>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent" colspan="3">
			<ext:equal value="M" property="sex">男</ext:equal><ext:equal value="F" property="sex">女</ext:equal>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在单位/部门</td>
		<td class="tdcontent" colspan="3">
			<html:hidden property="orgId"/>
			<ext:write property="orgFullName"/>
		</td>
	</tr>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">所属其他部门</td>
		<td class="tdcontent" colspan="3"><ext:write property="otherOrgNames"/></td>
	</tr>
</table>